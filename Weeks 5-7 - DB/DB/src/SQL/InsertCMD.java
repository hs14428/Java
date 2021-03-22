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
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();
        System.out.println("Hello InsertCMD class: nextToken = " + token);

        if (token.equals("INTO"))
        {
            System.out.println("After INTO");
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                System.out.println("After tablename:" + token);
//                dbServer.setTableName(token);
                tableName = token;
                System.out.println(tableName);
                token = dbServer.nextToken().toUpperCase();
                if (token.equals("VALUES"))
                {
                    System.out.println("After VALUES");
                    token = dbServer.nextToken();
                    if (token.matches(RegEx.BRACKETS.getRegex()))
                    {
                        System.out.println("After valuelist match");
                        ArrayList<Token> bracketsTokens;
                        bracketsTokens = dbServer.getBrackets(token);
                        System.out.println("db name:" + databaseName);
                        Table table = new Table(databaseName);
                        table.addRow(tableName, bracketsTokens);
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
