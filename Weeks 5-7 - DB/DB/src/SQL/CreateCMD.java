package SQL;

import DBExceptions.InvalidTokenException;
import Database.DBServer;

public class CreateCMD extends DBcmd
{

    public CreateCMD()
    {
        commandType = "CommandType";
        command = "CREATE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws InvalidTokenException
    {
        String token = dbServer.nextToken().toUpperCase();
        System.out.println("Hello CreateCMD class: nextToken = " + token);

        switch (token)
        {
            case("DATABASE"):
                return new CreateDatabaseCMD().runCommand(dbServer);
            case("TABLE"):
                return new CreateTableCMD().runCommand(dbServer);
        }
        System.out.println("CreateCMD runCommand() error.");
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
