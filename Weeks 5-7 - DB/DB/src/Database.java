import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Database
{
    private String currentDirectory;
    private String databaseName;
    private HashMap<String, Table> database;

    public Database(String databaseName)
    {
        this.database = new HashMap<String, Table>();
        this.databaseName = databaseName;
        currentDirectory = ".";
    }

// "Make" a database by creating a directory/folder in desired location
    public void createDatabase(String databaseName) throws IOException
    {
        File database = new File(currentDirectory + File.separator + databaseName);

        if (!database.exists()) {
            database.mkdirs();
        }
        else {
            System.out.println("Database of same name already exists.");
        }
    }

    public Table addTable(String tableName)
    {
        Table table = new Table(tableName, databaseName);
        table.createTable(tableName);
        return table;
    }

    public void listTables()
    {
        File databaseTables = new File(currentDirectory + File.separator + this.databaseName);
        File[] listOfFiles = databaseTables.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("Table: " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Database: " + listOfFile.getName());
            }
        }
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