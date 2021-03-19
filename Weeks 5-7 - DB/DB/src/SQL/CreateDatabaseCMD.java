package SQL;

import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;

public class CreateDatabaseCMD extends DBcmd
{

    public CreateDatabaseCMD()
    {
        commandType = "ID";
        command = "DATABASE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws InvalidTokenException
    {
        String token = dbServer.nextToken();
        System.out.println("Hello CreateDatabaseCMD class: nextToken = " + token);

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            databaseName = token;
            Database database = new Database(databaseName);
            database.createDatabase(databaseName);
            return "[OK] Database created";
        }
        // Add custom messages to ITE
        System.out.println("CreateDatabaseCMD runCommand() error.");
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
