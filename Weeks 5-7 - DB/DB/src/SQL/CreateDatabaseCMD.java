package SQL;

import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;

import java.io.IOException;

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
            try {
                database.createDatabase(databaseName);
                return "Database created.";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Add custom messages to ITE
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
