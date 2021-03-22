package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Table;
import Input.Token;

import java.io.IOException;
import java.util.ArrayList;

public class SelectCMD extends DBcmd
{
    public SelectCMD()
    {
        commandType = "CommandType";
        command = "SELECT";
        columnNames = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken();
        databaseName = dbServer.getDatabaseName();

        if (token.equals("*"))
        {
            return selectAll(dbServer);
        }
        else if (token.matches(RegEx.BRACKETS.getRegex()))
        {
            ArrayList<Token> bracketsTokens;
            bracketsTokens = dbServer.getBrackets(token);
            for (Token bracketsToken : bracketsTokens)
            {
                columnNames.add(bracketsToken.getTokenString());
            }
            return selectSome(dbServer, columnNames);
        }
        else {
            throw new InvalidTokenException(token);
        }
    }

    public String selectAll(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        Table table = new Table(databaseName);

        if (token.equals("FROM"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
//                table.readTable(token);
                String printTable = table.printTable(token);
                return "[OK]\n"+printTable;
            }
        }
        throw new InvalidTokenException(token);
    }

    public String selectSome(DBServer dbServer, ArrayList<String> columnNames) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        Table table = new Table(databaseName);

        if (token.equals("FROM"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
//                table.readTable(token);
                String printTable = table.printSome(token, columnNames);
                printTable = table.printTable(token);
                return "[OK]\n"+printTable;
            }
        }
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
