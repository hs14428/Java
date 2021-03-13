import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table
{
    private int primaryKey;
    private String tableName;
    private String databaseName;
    private ArrayList<Record> records;
    public ArrayList<List<String>> table;

    private Database database;
    private String tablePath;
    private String currentDirectory;

    // tableName is really just the .tab file name and database is really just the directory/folder
    public Table(String tableName, String databaseName)
    {
        this.tableName = tableName;
        this.databaseName = databaseName;
        database = new Database(databaseName);
        currentDirectory = database.getCurrentDirectory();
        tablePath = currentDirectory + File.separator + this.databaseName + File.separator + this.tableName + ".tab";
        System.out.println(tablePath);
    }

    public ArrayList<List<String>> ReadTable()
    {
        table = new ArrayList<List<String>>();
        File readTable = new File(tablePath);

        try {
            if (!readTable.exists()) {
                System.out.println("Table does not exists.");
            }
            else {
                FileReader reader = new FileReader(readTable);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                String[] lineArray;
                List<String> lineArrayList = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    lineArray = line.split("\t");
                    lineArrayList = Arrays.asList(lineArray);
                    table.add(lineArrayList);
                }
                reader.close();
                bufferedReader.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return table;
    }

    public void printTable()
    {
        table = ReadTable();
        for (List<String> strings : table) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
    }

}
