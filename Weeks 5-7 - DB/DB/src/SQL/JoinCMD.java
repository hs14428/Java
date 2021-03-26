package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JoinCMD extends DBcmd
{
    String token;
    String columnName;
    Database database;
    ArrayList<String> joinColumnNames;

    public JoinCMD()
    {
        commandType = "CommandType";
        command = "JOIN";
        tableNames = new ArrayList<>();
        joinColumnNames = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        databaseName = dbServer.getDatabaseName();
        token = dbServer.nextToken();
        tableName = token;
        checkValidTable();
        tableNames.add(token);
        token = dbServer.nextToken().toUpperCase();
        if (token.equals("AND"))
        {
            token = dbServer.nextToken();
            tableName = token;
            checkValidTable();
            tableNames.add(token);
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("ON"))
            {
                token = dbServer.nextToken();
                columnName = token;
                checkValidColumn();
                joinColumnNames.add(token);
                token = dbServer.nextToken().toUpperCase();
                if (token.equals("AND"))
                {
                    token = dbServer.nextToken();
                    columnName = token;
                    checkValidColumn();
                    joinColumnNames.add(token);
                    // NOT DONE
                    Table table = new Table(databaseName);
                    return "[OK]\n"+table.joinTables(tableNames.get(0), tableNames.get(1), joinColumnNames.get(0), joinColumnNames.get(1));
                }
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
        database = new Database(databaseName);
        tables = new HashMap<String, Table>();
        tables = database.scanDBForTables();
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
        throw new DatabaseException("[ERROR] - No columns in \""+tableName+"\" table match \""+columnName+"\" input");
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
