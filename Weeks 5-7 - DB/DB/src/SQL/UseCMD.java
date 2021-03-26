package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.MissingDatabaseException;
import Database.DBServer;
import Database.Database;

public class UseCMD extends DBcmd
{
    public UseCMD()
    {
        commandType = "CommandType";
        command = "USE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException
    {
        String token = dbServer.nextToken();

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            Database database = new Database(token);
            if (database.checkDatabaseExists())
            {
                databaseName = token;
                dbServer.setDatabaseName(databaseName);
                return "[OK] - "+databaseName+" database selected";
            }
        }
        throw new MissingDatabaseException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
