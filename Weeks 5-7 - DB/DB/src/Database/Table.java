package Database;

import DBExceptions.TableException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table
{
    private String tableName;
    private String databaseName;
    private String databasePath;
    private String currentDirectory;
    private String extension;
    private Database database;

    private ArrayList<ArrayList<String>> table;
    private ArrayList<Record> records;
    private Record record;
    private int numberOfRows;


    // tableName is really just the .tab file name and database is really just the directory/folder
    public Table(String databaseName)
    {
        records = new ArrayList<Record>();
        record = new Record();

        this.tableName = tableName;
        this.databaseName = databaseName;
        database = new Database(databaseName);
        currentDirectory = database.getCurrentDirectory();
        databasePath = currentDirectory + File.separator + this.databaseName;
        extension = ".tab";
    }

    public ArrayList<ArrayList<String>> readTable(String tableName)
    {
        File readTable = new File(databasePath + File.separator + tableName + extension);
        table = new ArrayList<ArrayList<String>>();

        try {
            if (!readTable.exists()) {
                System.out.println("Table does not exists.");
            }
            else {
                FileReader reader = new FileReader(readTable);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                String[] lineArray;
                while ((line = bufferedReader.readLine()) != null) {
                    lineArray = line.split("\t");
                    ArrayList<String> lineArrayList = new ArrayList<>(Arrays.asList(lineArray));
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

//  Add records to ArrayList<Database.Record> where Database. Record is a LinkedHashMap
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
    public void createTable(String tableName) throws IOException
    {
        File newTable = new File(databasePath + File.separator + tableName + extension);

        if (!newTable.exists())
        {
            newTable.createNewFile();
            FileWriter writer = new FileWriter(newTable);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
//            bufferedWriter.write("id" + "\t");
            bufferedWriter.write("id");
            bufferedWriter.write("\n");
//            bufferedWriter.write("1" + "\t");
            bufferedWriter.write("1");
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
            System.out.println("Table created.");
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" of same name already exists");
        }
    }

    public void removeTable(String tableName) throws IOException
    {
        File newTable = new File(databasePath + File.separator + tableName + extension);
        if (newTable.exists())
        {
            newTable.delete();
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" does not exist");
        }
    }

    public void writeToTable(String tableName) throws IOException
    {
        File writeToTable = new File(databasePath + File.separator + tableName + extension);

        if (!writeToTable.exists())
        {
            throw new TableException("[Error] - Table: "+tableName+" does not exists. Failed to write");
        }
        else {
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
        }
    }

    public void addColumn(String tableName, String columnName) throws IOException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);

        if (tableToAddTo.exists())
        {
            // Add column name to end of table columns
            table.get(0).add(columnName);
            // Go down each row and add an empty string at the end of each entry for new column
            for (int i = 1; i < table.size(); i++)
            {
                System.out.println(i);
                table.get(i).add("");
            }
            // Write new table back out to .tab file
            writeToTable(tableName);
            System.out.println("Column added.");
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" does not exist.");
        }
    }

    public void printTable(String tableName)
    {
        table = readTable(tableName);
        for (List<String> strings : table) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
    }

}
