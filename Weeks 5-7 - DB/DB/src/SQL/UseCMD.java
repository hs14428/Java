package SQL;

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
    public String runCommand(DBServer dbServer) throws MissingDatabaseException
    {
        String token = dbServer.nextToken();
        System.out.println("Hello UseCMD class: nextToken = " + token);

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            Database database = new Database(token);
            if (database.checkDatabaseExists()) {
                databaseName = token;
                dbServer.setDatabaseName(databaseName);
                return "[OK] - Database: "+databaseName+" selected";
            }
        }
        System.out.println("UseCMD runCommand() error.");
        throw new MissingDatabaseException(token);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
