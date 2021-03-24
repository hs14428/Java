package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;
import Input.Token;

import java.io.IOException;
import java.util.ArrayList;

public class CreateTableCMD extends DBcmd
{

    public CreateTableCMD()
    {
        commandType = "STRUCTURE";
        command = "TABLE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken();
        int queryLength = dbServer.getQueryLength();
//        System.out.println("Hello CreateTableCMD class: nextToken = " + token);

        // Need to add error handling for having not selected a DB?
//        if (databaseName.equals(""))
//        {
//            System.out.println("In .equals");
//            throw new InvalidTokenException(token);
//        }

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            tableName = token;
            databaseName = dbServer.getDatabaseName();
            System.out.println("in CreateTableCMD - dbname:"+databaseName);
            Database database = new Database(databaseName);
            database.addTable(token);
            if (queryLength == 4)
            {
                // Maybe split out to reduce complexity
                token = dbServer.nextToken();
                if (token.matches(RegEx.BRACKETS.getRegex()))
                {
                    ArrayList<Token> bracketsTokens;
                    bracketsTokens = dbServer.getBrackets(token);
                    Table newTable = new Table(databaseName);
                    newTable.addColumns(tableName, bracketsTokens);
                }
                else {
                    throw new InvalidTokenException(token);
                }
            }
            return "[OK] - "+tableName+" table created";
        }
        // Add custom messages to ITE
        System.out.println("CreateTableCMD runCommand() error.");
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
