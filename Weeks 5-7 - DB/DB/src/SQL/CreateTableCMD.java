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

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            token = dbServer.nextToken();

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
