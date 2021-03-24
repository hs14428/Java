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
//        if (!checkDatabaseExists())
//        {
//            throw new DatabaseException("[Error] - Database "+databaseName+" does not exists");
//        }
    }

    public HashMap<String, Table> scanDBForTables() throws DatabaseException
    {
        Table table;
        File[] tables = listTables();
        for (int i = 0; i < tables.length; i++)
        {
            table = new Table(databaseName, tables[i].getName());
            database.put(tables[i].getName(), table);
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
            throw new DatabaseException("[Error] - Database already exists");
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
            throw new DatabaseException("[Error] - Database does not exist");
        }
    }

    public boolean checkDatabaseExists()
    {
        File database = new File(currentDirectory + File.separator + databaseName);
        System.out.println("checkDatabaseExists: "+currentDirectory + File.separator + databaseName);
        if (database.exists())
        {
            return true;
        }
        return false;
    }

    public Table addTable(String tableName) throws IOException, DatabaseException
    {
        System.out.println("In addTable - dbName: "+databaseName);
        System.out.println("In addTable - tableName: "+tableName);

        Table table = new Table(databaseName);
        table.createTable(tableName);
        return table;
    }

    public File[] listTables()
    {
        File databaseTables = new File(currentDirectory + File.separator + databaseName);
        File[] listOfFiles = databaseTables.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("Table: " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Database: " + listOfFile.getName());
            }
        }
        return listOfFiles;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setCurrentDirectory(String directory)
    {
        currentDirectory = directory;
    }

    public String getCurrentDirectory()
    {
        return currentDirectory;
    }
}
