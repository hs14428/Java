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
        int tableNum = 0;
        System.out.println("Hello CreateTableCMD class: nextToken = " + token);

        // Need to add error handling for having not selected a DB
//        if (databaseName.equals(""))
//        {
//            System.out.println("In .equals");
//            throw new InvalidTokenException(token);
//        }

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            tableNames.add(token);
            tableNum = tableNames.size()-1;
            databaseName = dbServer.getDatabaseName();
            Database database = new Database(databaseName);
            database.addTable(token);
            // Change to if? and split up
            if (queryLength == 4)
            {
                token = dbServer.nextToken();
                System.out.println("in switch(4): "+token);
                if (token.matches(RegEx.BRACKETS.getRegex()))
                {
                    ArrayList<Token> bracketsTokens;
                    bracketsTokens = dbServer.getBrackets(token);
                    Table newTable = new Table(databaseName);
                    for (Token bracketsToken : bracketsTokens)
                    {
                        newTable.addColumn(tableNames.get(tableNum), bracketsToken.getTokenString());
                    }
                }
            }
//            switch (queryLength)
//            {
//                // If query length == 4 then column headers have been specified
//                case (4):
//                    token = dbServer.nextToken();
//                    System.out.println("in switch(4): "+token);
//                    if (token.matches(RegEx.BRACKETS.getRegex()))
//                    {
//                        ArrayList<Token> bracketsTokens;
//                        bracketsTokens = dbServer.getBrackets(token);
//                        Table newTable = new Table(databaseName);
//                        for (Token bracketsToken : bracketsTokens)
//                        {
//                            newTable.addColumn(tableNames.get(tableNum), bracketsToken.getTokenString());
//                        }
//                    }
//            }
            return "[OK] - "+tableNames.get(tableNum)+" table created";
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
