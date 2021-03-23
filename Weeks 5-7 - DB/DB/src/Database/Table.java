package Database;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import DBExceptions.TableException;
import Input.Token;
import SQL.Operator;
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

    private String columnName;
    private String operator;
    private String value;

    private ArrayList<ArrayList<String>> table;
    private ArrayList<Record> records;
    private Record record;
    private int numberOfRows;


    // tableName is really just the .tab file name and database is really just the directory/folder
    // Need to check that a DBServer folder exists and use this as the landing for the DB
    public Table(String databaseName) throws DatabaseException
    {
        records = new ArrayList<Record>();
        record = new Record();

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

    public ArrayList<ArrayList<String>> selectTable(String tableName, ArrayList<String> columnNames) throws IOException
    {
        table = readTable(tableName);
        addRecords();
        int size = columnNames.size();
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();

        for (String columnName : columnNames)
        {
            row.add(columnName);
        }
        newTable.add(row);
        for (int j = 0; j < records.size(); j++)
        {
            row = new ArrayList<>();
            for (int i = 0; i < size; i++)
            {
                row.add(records.get(j).getColumnData(columnNames.get(i)));
            }
            newTable.add(row);
        }
        table = newTable;
        return table;
    }

    public ArrayList<String> readColumnNames(String tableName) throws IOException
    {
        table = readTable(tableName);
        ArrayList<String> columnNames = new ArrayList<>();
        String columnName;

        for (int i = 0; i < table.get(0).size(); i++)
        {
            columnName = table.get(0).get(i);
            columnNames.add(columnName);
        }

        return columnNames;
    }

    public ArrayList<ArrayList<String>> conditionTable(ArrayList<ArrayList<String>> tableInput, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        System.out.println("try 1");
        table = tableInput;
        System.out.println("try 2");
        System.out.println(tableInput.get(0));
        System.out.println(printTable("contactdetaiils"));
        addRecords();
        System.out.println("try 3");

        columnName = conditions.get(conditionNum++);
        operator = conditions.get(conditionNum++);
        value = conditions.get(conditionNum++);
        System.out.println("conditionNum after assigning col, op and val: "+conditionNum);
        Operator op = new Operator(operator, value);

        if (op.isNumber())
        {
            return numberOperation();
        }
        if (!op.isNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            return stringBoolOperation();
        }
        throw new DatabaseException("[Error] - Could not carry out WHERE clause");
    }

    public ArrayList<ArrayList<String>> stringBoolOperation() throws DatabaseException, IOException
    {
        String entry;
        int i = 0;
        int size = table.size();
        ArrayList<ArrayList<String>> newTable = table;
        System.out.println(table.size());
        System.out.println("operator in bool op "+operator);
        System.out.println("value in bool op "+value);
        for (int j = 1; i < records.size(); i++, j++)
        {
            System.out.println("i = "+i+" and j = "+j);
            System.out.println(printTable("contactdetails"));
            entry = records.get(i).getColumnData(columnName);
            switch (operator)
            {
                case ("=="):
                    System.out.println(entry+" == "+value);
                    if (!entry.equals(value))
                    {
                        table.remove(j--);
                    }
                    break;
                case ("!="):
                    System.out.println("shouldnt be here");
                    if (entry.equals(value))
                    {
                        table.remove(j--);
                    }
                    break;
                default:
                    throw new DatabaseException("[Error] - "+operator+" is invalid");
            }
        }
        table = newTable;
        return table;
    }

    public ArrayList<ArrayList<String>> numberOperation()
    {
        String entry;
        for (int i = 0; i < records.size(); i++)
        {
            entry = records.get(i).getColumnData(columnName);
            switch (operator)
            {
                case ("=="):
                    if (!entry.equals(value))
                    {
                        table.remove(i);
                    }
                case (">"):

                case ("<"):

                case (">="):

                case ("<="):

                case ("!="):
                    if (entry.equals(value))
                    {
                        table.remove(i);
                    }
                case ("LIKE"):

            }
        }
        return table;
    }

    public ArrayList<ArrayList<String>> getTable() { return table ; }
}
