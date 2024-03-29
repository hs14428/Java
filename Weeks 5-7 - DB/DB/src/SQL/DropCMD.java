package SQL;

import DBExceptions.DatabaseException;
import Database.DBServer;
import Database.Database;
import Database.Table;

import java.io.IOException;

public class DropCMD extends DBcmd
{

    public DropCMD()
    {
        commandType = "CommandType";
        command = "DROP";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();

        switch (token)
        {
            case("DATABASE"):
                token = dbServer.nextToken();
                if (databaseName == null)
                {
                    databaseName = token;
                }
                if (!token.equals(databaseName))
                {
                    throw new DatabaseException("[ERROR] - Selected database and database to be dropped don't match");
                }
                Database databaseToDrop = new Database(token);
                databaseToDrop.removeDatabase(token);
                return "[OK] - "+token+" database dropped";
            case("TABLE"):
                token = dbServer.nextToken();
                Table tableToDrop = new Table(databaseName);
                tableToDrop.removeTable(token);
                return "[OK] - "+token+" table dropped";
            default:
                throw new DatabaseException("[ERROR] - Table/Database could not be dropped");
        }
    }

    @Override
    public String getCommand() {
        return command;
    }
}
