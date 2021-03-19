package SQL;

import DBExceptions.InvalidTokenException;
import Database.DBServer;

public class CreateTableCMD extends DBcmd
{

    public CreateTableCMD()
    {
        commandType = "ID";
        command = "TABLE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws InvalidTokenException
    {
        String token = dbServer.nextToken();
        System.out.println("Hello CreateTableCMD class: nextToken = " + token);
        System.out.println(dbServer.getDatabaseName());
        return null;
        // Add custom messages to ITE
//        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
