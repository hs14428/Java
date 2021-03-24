package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateCMD extends DBcmd
{
    Database database;
    String token;
    String columnName;

    public UpdateCMD()
    {
        commandType = "CommandType";
        command = "UPDATE";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        databaseName = dbServer.getDatabaseName();
        database = new Database(databaseName);
        token = dbServer.nextToken();
        System.out.println("Hello UpdateCMD class: nextToken = " + token);

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            tableName = token;
            dbServer.setTableName(tableName);
            checkValidTable();
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("SET"))
            {
                token = dbServer.nextToken();
                columnName = token;
                checkValidColumn();
                token = dbServer.nextToken().toUpperCase();
                if (token.equals("WHERE"))
                {

                }
            }
        }
        throw new InvalidTokenException(token);
    }

    public void checkValidTable() throws DatabaseException, IOException
    {
        getTableNames();
        if (!tables.containsKey(tableName))
        {
            throw new DatabaseException("[Error] - No tables match \""+tableName+"\" input");
        }
    }

    public void checkValidColumn() throws DatabaseException, IOException
    {
        getColumnNames(tableName);

        for (String name : columnNames)
        {
            if (name.equals(columnName))
            {
                return;
            }
        }
        throw new DatabaseException("[Error] - No columns in \""+tableName+"\" table match \""+columnName+"\" input");
    }

    public void getTableNames() throws DatabaseException, IOException
    {
        tables = database.scanDBForTables();
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnNames = new ArrayList<>();
        columnNames = table.readColumnNames(tableName);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
