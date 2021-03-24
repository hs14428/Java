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
        System.out.println("in Table - dbPath: " + databasePath);
        extension = ".tab";
    }

//  Second constructor for creating HashMap of tables in database
    public Table(String databaseName, String tableName) throws DatabaseException
    {
        records = new ArrayList<Record>();
        record = new Record();

        this.tableName = tableName;
        this.databaseName = databaseName;
        database = new Database(databaseName);
        currentDirectory = database.getCurrentDirectory();
        databasePath = currentDirectory + File.separator + this.databaseName;
        System.out.println("in Table - dbPath: " + databasePath);
        extension = ".tab";
    }

    public ArrayList<ArrayList<String>> readTable(String tableName) throws IOException
    {
        File readTable = new File(databasePath + File.separator + tableName + extension);
        table = new ArrayList<ArrayList<String>>();

        if (!readTable.exists())
        {
            throw new TableException("[Error] - "+tableName +" does not exist.");
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
        //Change to String[]?
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

    public void recordsToTable() throws IOException
    {
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();
        System.out.println("recordsToTable tableName = "+tableName);
        ArrayList<String> columnNames = readColumnNames(tableName);
        System.out.println("col: "+ columnNames.get(0));
        ArrayList<String> row;
        int size = columnNames.size();
        newTable.add(columnNames);

        for (int j = 0; j < records.size(); j++)
        {
            row = new ArrayList<>();
            for (String name : columnNames)
            {
                row.add(records.get(j).getColumnData(name));
            }
            newTable.add(row);
        }
        table = newTable;
    }

    public ArrayList<Record> getRecords()
    {
        return records;
    }

//  "Make" a database by creating a directory/folder in desired location
    public void createTable(String tableName) throws IOException
    {
        File newTable = new File(databasePath + File.separator + tableName + extension);
        System.out.println("in createTable - filepath: "+databasePath + File.separator + tableName + extension);

        if (!newTable.exists())
        {
            if (!newTable.createNewFile())
            {
                throw new TableException("[Error] - Cannot find the path specified");
            }

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
                System.out.println("in addColumns - columnName = "+columnName.getTokenString());
//                // Check the contents of the columnNames arraylist are valid
//                Pattern p = Pattern.compile(RegEx.VARIABLENAME.getRegex());
//                Matcher m = p.matcher(columnName.)
                if (!columnName.getTokenString().matches(RegEx.VARIABLENAME.getRegex()))
                {
                    System.out.println("shouldnt be here");
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
            for (String name : columnNames)
            {
                row.add(records.get(j).getColumnData(name));
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

    public void updateTable(String tableName, ArrayList<String> updateValues, ArrayList<String> conditions, int conditionNum) throws IOException, DatabaseException
    {
        table = readTable(tableName);
        this.tableName = tableName;
        addRecords();

        columnName = conditions.get(conditionNum++);
        operator = conditions.get(conditionNum++);
        System.out.println("operator "+operator);
        value = conditions.get(conditionNum++);
        Operator op = new Operator(operator, value);
        op.numberOrString();

        if (op.isNumber())
        {
            System.out.println("Passed op.isNumber() check (ie. number)");
//            updateNumberOp();
        }
        else if (!op.isNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            updateStringBoolOp(updateValues);
        }
        else {
            throw new DatabaseException("[Error] - Could not update table. Check inputs.");
        }
    }

    private void updateStringBoolOp(ArrayList<String> updateValues) throws DatabaseException, IOException
    {
//      While loop for if there are multiple values to update
        while (updateValues.size() != 0)
        {
            int j = 1;
            for (int i = 0; i < records.size(); i++, j++)
            {
                String updateColumnName = updateValues.get(0);
                String updateValue = updateValues.get(1);
                switch(operator)
                {
                    case ("=="):
                        if (records.get(i).getColumnData(columnName).equals(value))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case ("!="):
                        if (!records.get(i).getColumnData(columnName).equals(value))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    default:
                        throw new DatabaseException("[Error] - Error updating the table.");
                }
            }
            // Remove the update value pairs and "bring forward" the next pair (if there is more)
            updateValues.remove(0);
            updateValues.remove(0);
        }
    }

    private void updateTableValues(int i, String updateColumnName, String updateValue) throws IOException
    {
        records.get(i).addToRecords(updateColumnName, updateValue);
        recordsToTable();
        writeToTable(tableName);
    }

//    public ArrayList<ArrayList<String>> conditionTable(ArrayList<ArrayList<String>> tableInput, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    public String conditionTable(ArrayList<ArrayList<String>> tableInput, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        table = tableInput;
        addRecords();

        columnName = conditions.get(conditionNum++);
        operator = conditions.get(conditionNum++);
        value = conditions.get(conditionNum++);
        Operator op = new Operator(operator, value);
//      Check if column being operator on is a String/Bool or Int/Float type column and then
//      perform the correct operation on it
        op.numberOrString();

        if (op.isNumber())
        {
            System.out.println("Passed op.isNumber() check (ie. number)");
            selectNumberOp();
            return printTable(tableName);
//            return numberOperation();
        }
        if (!op.isNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            selectStringBoolOp();
            return printTable(tableName);
//            return stringBoolOperation();
        }
        throw new DatabaseException("[Error] - Could not carry out WHERE clause. Check inputs.");
    }

//  Method for operating on Strings and Bools for the SELECT command
//    public ArrayList<ArrayList<String>> stringBoolOperation() throws DatabaseException, IOException
    public void selectStringBoolOp() throws DatabaseException, IOException
    {
        String entry;
        int j = 1;
        int size = table.size();
        System.out.println(table.size());
//        System.out.println("operator in bool op "+operator);
//        System.out.println("value in bool op "+value);
        for (int i = 0; i < records.size(); i++, j++)
        {
//            System.out.println("i = "+i+" and j = "+j);
            System.out.println(printTable(tableName));
            entry = records.get(i).getColumnData(columnName);

            switch (operator)
            {
                case ("=="):
                    if (!entry.equals(value))
                    {
                        table.remove(j--);
                    }
                    break;
                case ("!="):
                    if (entry.equals(value))
                    {
                        table.remove(j--);
                    }
                    break;
                default:
                    throw new DatabaseException("[Error] - "+operator+" is invalid");
            }
        }
    }

//  Method for handling operations on ints and floats for the SELECT command
//    public ArrayList<ArrayList<String>> numberOperation() throws DatabaseException
    public void selectNumberOp() throws DatabaseException
    {
        String entry;
        float floatValue = Float.parseFloat(value);
        float floatEntry;
        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            entry = records.get(i).getColumnData(columnName);
            floatEntry = Float.parseFloat(entry);
            j = selectFloatOp(operator, floatEntry, floatValue, j);
        }
    }

//  Method for comparing floats and removing rows of table based on operator for the SELECT command
    public int selectFloatOp(String operator, Float entry, Float value, int j) throws DatabaseException
    {
        int index = j;
        System.out.println(operator);
        Operator op = new Operator();
        switch (operator)
        {
            case("=="):
                if (!op.floatEqualsTo(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            case("!="):
                if (op.floatEqualsTo(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            case(">"):
                if (!op.floatGreaterThan(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            case("<"):
                if (!op.floatLessThan(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            // needs testing
            case (">="):
                if (!op.floatGreaterThanEqual(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            // needs testing
            case ("<="):
                if (!op.floatLessThanEqual(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            case ("LIKE"):
                return index;
            default:
                throw new DatabaseException("[Error] - "+operator+" is invalid");
        }
    }

    public ArrayList<ArrayList<String>> getTable() { return table ; }
}
