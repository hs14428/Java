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
    ArrayList<String> columnNamesToAddTo = new ArrayList<>();
    String token;

    public InsertCMD()
    {
        commandType = "CommandType";
        command = "INSERT";
        columnNames = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();

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
                    if (token.matches(RegEx.BRACKETS.getRegex()))
                    {
                    return addRowsToTable(dbServer);
                    }
                }
            }
        }
        throw new InvalidTokenException(token);
    }

    public String addRowsToTable(DBServer dbServer) throws DatabaseException, IOException
    {
        ArrayList<Token> bracketsTokens;
        bracketsTokens = dbServer.getBrackets(token);
        // columnNames.size()-1 because of the id column
        getColumnNames(tableName);
        if (bracketsTokens.size() == columnNames.size()-1)
        {
            for (Token bracketsToken : bracketsTokens)
            {
                columnNamesToAddTo.add(bracketsToken.getTokenString());
            }
            Table table = new Table(databaseName);
            table.addRow(tableName, columnNamesToAddTo);
            return "[OK] - Values inserted";
        }
        throw new DatabaseException("[ERROR] - Too many/few rows chosen");
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnNames = new ArrayList<>();
        columnNames = table.readColumnNames(tableName);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
