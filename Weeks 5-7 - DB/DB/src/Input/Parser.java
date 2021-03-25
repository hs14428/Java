package Input;

import DBExceptions.InvalidTokenException;
import Database.DBServer;
import SQL.*;

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
        DBcmd drop = new DropCMD();
        DBcmd insert = new InsertCMD();
        DBcmd select = new SelectCMD();
        DBcmd update = new UpdateCMD();
        DBcmd delete = new DeleteCMD();
        DBcmd alter = new AlterCMD();
        DBcmd join = new JoinCMD();
        commandType = new DBcmd[]{create, use, drop, insert, select, update, delete, alter, join};
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
