package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;

import java.io.IOException;
import java.util.HashMap;

public class DeleteCMD extends DBcmd
{
    Database database;
    String token;

    public DeleteCMD()
    {
        commandType = "CommandType";
        command = "DELETE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        databaseName = dbServer.getDatabaseName();
        database = new Database(databaseName);
        token = dbServer.nextToken().toUpperCase();

        if (token.equals("FROM"))
        {
            token = dbServer.nextToken();
            tableName = token;
            dbServer.setTableName(tableName);
            checkValidTable();
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("WHERE"))
            {
                dbServer.decCurrentTokenNum();
                return new ConditionCMD(command).runCommand(dbServer);
            }
        }
        throw new InvalidTokenException(token);
    }

    public void checkValidTable() throws DatabaseException
    {
        getTableNames();
        if (!tables.containsKey(tableName))
        {
            throw new DatabaseException("[ERROR] - No tables match \""+tableName+"\" input");
        }
    }

    public void getTableNames() throws DatabaseException
    {
        tables = new HashMap<String, Table>();
        tables = database.scanDBForTables();
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
