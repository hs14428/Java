package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;

public class CreateDatabaseCMD extends DBcmd
{

    public CreateDatabaseCMD()
    {
        commandType = "STRUCTURE";
        command = "DATABASE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException
    {
        String token = dbServer.nextToken();

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            databaseName = token;
            Database database = new Database(databaseName);
            database.createDatabase(databaseName);
            return "[OK] - "+databaseName+" database created";
        }
        throw new InvalidTokenException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
