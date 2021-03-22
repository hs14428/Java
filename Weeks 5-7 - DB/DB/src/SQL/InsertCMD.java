package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Table;
import Input.Token;

import java.io.IOException;
import java.util.ArrayList;

public class InsertCMD extends DBcmd
{

    public InsertCMD()
    {
        commandType = "CommandType";
        command = "INSERT";
        columnNames = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();
        System.out.println("Hello InsertCMD class: nextToken = " + token);

        if (token.equals("INTO"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                tableName = token;
                token = dbServer.nextToken().toUpperCase();
                if (token.equals("VALUES"))
                {
                    token = dbServer.nextToken();
                    // Need to add a check for the amount of things inside bracks == num columns
                    if (token.matches(RegEx.BRACKETS.getRegex()))
                    {
                        ArrayList<Token> bracketsTokens;
                        bracketsTokens = dbServer.getBrackets(token);
                        // Can cut out to reduce complexity
                        for (Token bracketsToken : bracketsTokens)
                        {
                            columnNames.add(bracketsToken.getTokenString());
                        }
                        Table table = new Table(databaseName);
                        table.addRow(tableName, columnNames);
                        return "[OK] - Values inserted";
                    }
                }
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
