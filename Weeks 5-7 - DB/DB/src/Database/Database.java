package Database;

import DBExceptions.DatabaseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Database
{
    private String currentDirectory;
    private String databaseName;
    private HashMap<String, Table> database;

    public Database(String databaseName) throws DatabaseException
    {
        this.database = new HashMap<String, Table>();
        this.databaseName = databaseName;
        currentDirectory = "."+File.separator+"Databases";
    }

    public HashMap<String, Table> scanDBForTables() throws DatabaseException
    {
        Table table;
        String tableName;
        File[] tables = listTables();
        for (File file : tables)
        {
            tableName = file.getName().split("\\.")[0];
            table = new Table(databaseName, tableName);
            database.put(tableName, table);
        }
        return database;
    }

// "Make" a database by creating a directory/folder in desired location
    public void createDatabase(String databaseName) throws DatabaseException
    {
        this.databaseName = databaseName;
        File database = new File(currentDirectory + File.separator + databaseName);

        if (!database.exists())
        {
            database.mkdirs();
        }
        else {
            throw new DatabaseException("[ERROR] - Database already exists");
        }
    }

    public void removeDatabase(String databaseName) throws DatabaseException
    {
        File database = new File(currentDirectory + File.separator + databaseName);
        File[] tableNames = listTables();

        if (database.exists())
        {
            for (File table : tableNames)
            {
                table.delete();
            }
            database.delete();
        }
        else {
            throw new DatabaseException("[ERROR] - Database does not exist");
        }
    }

    public boolean checkDatabaseExists()
    {
        File database = new File(currentDirectory + File.separator + databaseName);
        if (database.exists())
        {
            return true;
        }
        return false;
    }

    public Table addTable(String tableName) throws IOException, DatabaseException
    {
        Table table = new Table(databaseName);
        table.createTable(tableName);
        return table;
    }

    public File[] listTables()
    {
        File databaseTables = new File(currentDirectory + File.separator + databaseName);
        File[] listOfFiles = databaseTables.listFiles();
        assert listOfFiles != null;
        return listOfFiles;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public String getCurrentDirectory()
    {
        return currentDirectory;
    }
}
