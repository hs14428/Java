import java.io.*;
import java.util.*;

public class Table
{
    private String tableName;
    private String tablePath;
    private String databaseName;
    private String databasePath;
    private String currentDirectory;
    private String extension;
    private Database database;

    public ArrayList<List<String>> table;
    public ArrayList<Record> records;
    private Record record;


    // tableName is really just the .tab file name and database is really just the directory/folder
    public Table(String tableName, String databaseName)
    {
        table = new ArrayList<List<String>>();
        records = new ArrayList<Record>();
        record = new Record();

        this.tableName = tableName;
        this.databaseName = databaseName;
        database = new Database(databaseName);
        currentDirectory = database.getCurrentDirectory();
        databasePath = currentDirectory + File.separator + this.databaseName;
        extension = ".tab";
        tablePath = databasePath + File.separator + this.tableName + extension;
    }

    public ArrayList<List<String>> readTable()
    {
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

//  Add records to ArrayList<Record> where Record is a LinkedHashMap
//  Allows for easier record finding
    public void addRecords()
    {
        int numberOfColumns = table.get(0).size();
        int numberOfRows = table.size();
        Object[] columnNames = table.get(0).toArray();
        for (int i = 0; i < numberOfRows-1; i++) {
            Object[] rowData = table.get(i+1).toArray();
            record = new Record();
            for (int j = 0; j < numberOfColumns; j++) {
                String columnData = (String) rowData[j];
                String columnName = (String) columnNames[j];
                record.addToRecords(columnName, columnData);
            }
            records.add(i, record);
        }
    }

    public ArrayList<Record> getRecords()
    {
        return records;
    }

//  "Make" a database by creating a directory/folder in desired location
    public void createTable(String tableName)
    {
        File table = new File(databasePath + File.separator + tableName + extension);

        if (!table.exists()) {
            try {
                table.createNewFile();
                System.out.println("Table created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Table of same name already exists.");
        }
    }

    public void writeToTable(String tableName)
    {
        File writeToTable = new File(databasePath + File.separator + tableName + extension);

        if (!writeToTable.exists()) {
            System.out.println("\nTable does not exist.");
        }
        else {
            try {
                FileWriter writer = new FileWriter(writeToTable);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                for (List<String> strings : table) {
                    for (String string : strings) {
                        bufferedWriter.write(string + "\t");
                    }
                    bufferedWriter.write("\n");
                }
                bufferedWriter.flush();
                bufferedWriter.close();
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred: " + e + ".\nFailed to write to file.");
                e.printStackTrace();
            }
        }
    }

    public void printTable()
    {
        table = readTable();
        for (List<String> strings : table) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
    }

}
