package Input;

import DBExceptions.InvalidTokenException;
import Database.DBServer;
import SQL.CreateCMD;
import SQL.DBcmd;
import SQL.UseCMD;

// Parser needs to firstly check that the input string is valid before any commands
public class Parser
{
    private DBServer dbServer;
    private DBcmd[] commandType;

    public Parser(DBServer dbServer)
    {
        this.dbServer = dbServer;
        DBcmd create = new CreateCMD();
        DBcmd use = new UseCMD();
        commandType = new DBcmd[]{create, use};
    }

    public DBcmd parse() throws InvalidTokenException
    {
        int currentToken = dbServer.getCurrentTokenNum();
        String token = dbServer.getTokens().get(currentToken).getTokenString().toUpperCase();
        System.out.println("parse() token: " + token);
        for (DBcmd command : commandType)
        {
            if (command.getCommand().equals(token))
            {
                return command;
            }
        }
        System.out.println("Parser parse() error.");
        throw new InvalidTokenException(token);
    }
}

//        System.out.println(token);
////        switch (token)
////        {
////            case "CREATE":
//////                CreateCMD create = new CreateCMD(dbServer);
//////                create.runCommand();
////                return new CreateCMD();
//////                return new CreateCMD(dbServer);
////            case "USE":
//////                return new UseCommand(query);
////            case "DROP":
////            case "ALTER":
////            case "INSERT":
////            case "SELECT":
////            case "UPDATE":
////            case "DELETE":
////            case "JOIN":
////        }
