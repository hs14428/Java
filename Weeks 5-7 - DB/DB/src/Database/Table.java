package Database;

import DBExceptions.InvalidTokenException;
import DBExceptions.TableException;
import Input.Token;
import SQL.RegEx;

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
    // Need to check that a DBServer folder exists and use this as the landing for the DB
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

    public ArrayList<ArrayList<String>> readTable(String tableName) throws IOException
    {
        File readTable = new File(databasePath + File.separator + tableName + extension);
        table = new ArrayList<ArrayList<String>>();

        if (!readTable.exists())
        {
            throw new TableException("[Error] - Table does not exist.");
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
        return table;
    }

//  Adds the contents of a table into a Record hashmap format
//  Allows for easier record finding
    public void addRecords()
    {
        records = new ArrayList<Record>();
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
            bufferedWriter.write("id");
//            bufferedWriter.write("\n");
//            bufferedWriter.write("1");
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

    public void addColumns(String tableName, ArrayList<Token> columnNames) throws IOException, InvalidTokenException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);

        if (tableToAddTo.exists())
        {
            for (Token columnName : columnNames)
            {
                // Check the contents of the columnNames arraylist are valid
                if (!columnName.toString().matches(RegEx.VARIABLENAME.getRegex()))
                {
                    throw new InvalidTokenException(columnName.getTokenString());
                }
                // Add column name to end of table columns
                table.get(0).add(columnName.getTokenString());
                // Go down each row and add an empty string at the end of each entry for new column
                for (int i = 1; i < table.size(); i++)
                {
                    table.get(i).add("");
                }
                // Write new table back out to .tab file
                writeToTable(tableName);
                System.out.println("Column added.");
            }
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" does not exist.");
        }
    }

    public void addRow(String tableName, ArrayList<String> rowValues) throws IOException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);
        String newIdNum = String.valueOf(table.size());

        if (tableToAddTo.exists())
        {
            rowValues.add(0, newIdNum);
            table.add(rowValues);
            writeToTable(tableName);
            System.out.println("Row added.");
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" does not exist.");
        }
    }

    public String printTable(String tableName) throws IOException
    {
//        table = readTable(tableName);
        String tableString = "";
        for (ArrayList<String> strings : table) {
            for (String string : strings) {
                tableString += string;
                tableString += "\t";
            }
            tableString += "\n";
        }
        return tableString;
    }

    public String printSome(String tableName, ArrayList<String> columnNames) throws IOException
    {
        table = readTable(tableName);
        addRecords();
        int size = columnNames.size();
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();

//        String newTableString = "";
        for (String columnName : columnNames)
        {
//            newTableString += columnName;
//            newTableString += "\t";
            row.add(columnName);
        }
        newTable.add(row);
//        newTableString += "\n";
        for (int j = 0; j < records.size(); j++)
        {
            row = new ArrayList<>();
            for (int i = 0; i < size; i++)
            {
//                columnNames.add(records.get(j).getColumnData(columnNames.get(i)));
//                newTableString += records.get(j).getColumnData(columnNames.get(i));
//                newTableString += "\t";
                row.add(records.get(j).getColumnData(columnNames.get(i)));
            }
//            newTableString += "\n";
            newTable.add(row);
        }
        table = newTable;
        return "";
    }
}
