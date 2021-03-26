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

    private String conditionColumnName;
    private String conditionOperator;
    private String conditionValue;

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

        newTable.add(columnNames);
        for (Record item : records)
        {
            row = new ArrayList<>();
            for (String name : columnNames)
            {
                row.add(item.getColumnData(name));
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

        if (!newTable.exists())
        {
            if (!newTable.createNewFile())
            {
                throw new TableException("[Error] - Cannot find the path specified");
            }

            FileWriter writer = new FileWriter(newTable);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("id");
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
//              Check the contents of the columnNames arraylist are valid
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
                    table.get(i).add(" ");
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

    public void removeColumn(String tableName, String columnToDrop) throws IOException, InvalidTokenException
    {
        File tableToRemoveToFrom = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);
        int tableColumnSize = table.get(0).size();
        int tableColumnIndex = 0;

        if (tableToRemoveToFrom.exists())
        {
//          Loop through columns and find index of column to drop
            for (int i = 0; i < tableColumnSize; i++)
            {
                if (table.get(0).get(i).equals(columnToDrop))
                {
                    tableColumnIndex = i;
                }
            }
//          Remove column name from end of table columns
            System.out.println("tCI: "+tableColumnIndex);
            table.get(0).remove(tableColumnIndex);
//          Go down each row and add an empty string at the end of each entry for new column
            for (int i = 1; i < table.size(); i++)
            {
                System.out.println("table row size: "+table.size());
                System.out.println("table col size: "+table.get(i).size());
                System.out.println("in loop");
                System.out.println("tableColumnIndex-1 val: "+table.get(i).get(tableColumnIndex-1));
                System.out.println("tableColumnIndex val: "+table.get(i).get(tableColumnIndex));
                table.get(i).remove(tableColumnIndex);
            }
            // Write new table back out to .tab file
            writeToTable(tableName);
            System.out.println("Column dropped.");
        }
        else {
            throw new TableException("[Error] - Table: "+tableName+" does not exist.");
        }
    }

    public void addRow(String tableName, ArrayList<String> rowValues) throws IOException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);
        addRecords();
        String dynamicId;
        int intDynamicId; // = Integer.parseInt(dynamicId);

        if (records.size() == 0)
        {
            intDynamicId = 1;
        }
        else {
            dynamicId = records.get(records.size()-1).getColumnData("id");
            intDynamicId = Integer.parseInt(dynamicId);
            intDynamicId += 1;
        }

        System.out.println("dynamic int: "+intDynamicId);

//        String newIdNum = String.valueOf(table.size());
        String newIdNum = String.valueOf(intDynamicId);

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

//    public ArrayList<ArrayList<String>> selectTable(String tableName, ArrayList<String> columnNames) throws IOException
    public ArrayList<ArrayList<String>> trimTable(ArrayList<ArrayList<String>> table, ArrayList<String> columnNames) throws IOException
    {
        this.table = table;
//        table = readTable(tableName);
        addRecords();
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>(columnNames);

        newTable.add(row);
        for (Record item : records)
        {
            row = new ArrayList<>();
            for (String name : columnNames)
            {
                row.add(item.getColumnData(name));
            }
            newTable.add(row);
        }
        this.table = newTable;
        return this.table;
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

    public String joinTables(String tableName) throws IOException
    {
        this.tableName = tableName;
        return printTable(tableName);
    }

    public void deleteRowsFromTable(String tableName, ArrayList<String> conditions, int conditionNum) throws IOException, DatabaseException
    {
        table = readTable(tableName);
        this.tableName = tableName;
        addRecords();

        conditionColumnName = conditions.get(conditionNum++);
        conditionOperator = conditions.get(conditionNum++);
        conditionValue = conditions.get(conditionNum++);
        Operator op = new Operator(conditionOperator, conditionValue);
        op.valueNumberOrString();

        if (op.isValueNumber())
        {
            System.out.println("Passed op.isNumber() check (ie. number)");
            deleteNumberOp();
        }
        else if (!op.isValueNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            deleteStringBoolOp();
        }
        else {
            throw new DatabaseException("[Error] - Could not update table. Check inputs.");
        }
    }

    private void deleteNumberOp() throws DatabaseException, IOException
    {
        float floatCondValue = Float.parseFloat(conditionValue);
        Operator op = new Operator();

        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            System.out.println("records.size(): "+records.size());
            System.out.println(printTable(tableName));
            float floatCondColumnData = Float.parseFloat(records.get(i).getColumnData(conditionColumnName));
            System.out.println("floatCCD: "+floatCondColumnData+" fCV: "+floatCondValue);
            switch(conditionOperator)
            {
                case ("=="):
                    if (op.floatEqualsTo(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                case ("!="):
                    if (!op.floatEqualsTo(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                case(">"):
                    System.out.println(floatCondColumnData+" > "+floatCondValue);
                    if (op.floatGreaterThan(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                case("<"):
                    if (op.floatLessThan(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                // needs testing
                case (">="):
                    if (op.floatGreaterThanEqual(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                // needs testing
                case ("<="):
                    if (op.floatLessThanEqual(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                default:
                    throw new DatabaseException("[Error] - Error deleting from the table.");
            }
        }
    }

    private void deleteStringBoolOp() throws DatabaseException, IOException
    {
        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            System.out.println("records.size(): "+records.size());
            System.out.println(printTable(tableName));
            switch(conditionOperator)
            {
                case ("=="):
                    if (records.get(i).getColumnData(conditionColumnName).equals(conditionValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                case ("!="):
                    if (!records.get(i).getColumnData(conditionColumnName).equals(conditionValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                default:
                    throw new DatabaseException("[Error] - Error deleting from the table.");
            }
        }
    }

    private void deleteTableValues(int i) throws IOException
    {
        records.remove(i);
        recordsToTable();
        writeToTable(tableName);
    }

    public void updateTable(String tableName, ArrayList<String> updateValues, ArrayList<String> conditions, int conditionNum) throws IOException, DatabaseException
    {
        table = readTable(tableName);
        this.tableName = tableName;
        addRecords();

        conditionColumnName = conditions.get(conditionNum++);
        conditionOperator = conditions.get(conditionNum++);
        System.out.println("operator "+ conditionOperator);
        conditionValue = conditions.get(conditionNum++);
        Operator op = new Operator(conditionOperator, conditionValue);
        op.valueNumberOrString();

        if (op.isValueNumber())
        {
            System.out.println("Passed op.isNumber() check (ie. number)");
            updateNumberOp(updateValues);
        }
        else if (!op.isValueNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            updateStringBoolOp(updateValues);
        }
        else {
            throw new DatabaseException("[Error] - Could not update table. Check inputs.");
        }
    }

    public void updateNumberOp(ArrayList<String> updateValues) throws DatabaseException, IOException
    {
        float floatCondValue = Float.parseFloat(conditionValue);
        Operator op = new Operator();

        System.out.println(updateValues.size());
        //      While loop for if there are multiple values to update
        while (updateValues.size() != 0)
        {
            int j = 1;
            for (int i = 0; i < records.size(); i++, j++)
            {
                String updateColumnName = updateValues.get(0);
                String updateValue = updateValues.get(1);
                float floatCondColumnData = Float.parseFloat(records.get(i).getColumnData(conditionColumnName));

                switch(conditionOperator)
                {
                    case ("=="):
                        if (op.floatEqualsTo(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case ("!="):
                        if (!op.floatEqualsTo(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case(">"):
                        System.out.println(floatCondColumnData+" > "+floatCondValue);
                        if (op.floatGreaterThan(floatCondColumnData, floatCondValue))
                        {
                            System.out.println("made it in > ");
                            System.out.println("col = "+updateColumnName+" val = "+updateValue);
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case("<"):
                        if (op.floatLessThan(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    // needs testing
                    case (">="):
                        if (op.floatGreaterThanEqual(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    // needs testing
                    case ("<="):
                        if (op.floatLessThanEqual(floatCondColumnData, floatCondValue))
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
                switch(conditionOperator)
                {
                    case ("=="):
                        if (records.get(i).getColumnData(conditionColumnName).equals(conditionValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case ("!="):
                        if (!records.get(i).getColumnData(conditionColumnName).equals(conditionValue))
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

//    public String selectConditionTable(ArrayList<ArrayList<String>> tableInput, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    public String selectTable(String tableName, ArrayList<String> columnNames, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        this.tableName = tableName;
        System.out.println("Table name in selectConditionTable "+tableName);
        table = readTable(tableName);
        addRecords();

        conditionColumnName = conditions.get(conditionNum++);
        conditionOperator = conditions.get(conditionNum++);
        conditionValue = conditions.get(conditionNum++);
        Operator op = new Operator(conditionOperator, conditionValue);
//      Check if column being operator on is a String/Bool or Int/Float type column and then
//      perform the correct operation on it
        op.valueNumberOrString();

        if (op.isValueNumber())
        {
            System.out.println("Passed op.isNumber() check (ie. number)");
            selectNumberOp();
            trimTable(table, columnNames);
            return printTable(tableName);
        }
        if (!op.isValueNumber())
        {
            System.out.println("Passed !op.isNumber() check");
            selectStringBoolOp();
            trimTable(table, columnNames);
            return printTable(tableName);
        }
        throw new DatabaseException("[Error] - Could not carry out select WHERE clause. Check inputs.");
    }

//  Method for operating on Strings and Bools for the SELECT command
//    public ArrayList<ArrayList<String>> stringBoolOperation() throws DatabaseException, IOException
    public void selectStringBoolOp() throws DatabaseException, IOException
    {
        String entry;
        int j = 1;
        int size = table.size();
        System.out.println(table.size());
        System.out.println("operator in bool op "+ conditionOperator);
        System.out.println("value in bool op "+ conditionValue);
        for (int i = 0; i < records.size(); i++, j++)
        {
            System.out.println("i = "+i+" and j = "+j);
            System.out.println(printTable(tableName));
            entry = records.get(i).getColumnData(conditionColumnName);
            System.out.println(entry+" = "+ conditionValue);

            switch (conditionOperator)
            {
                case ("=="):
                    if (!entry.equals(conditionValue))
                    {
                        table.remove(j--);
//                        records.remove(i--);
//                        records.get(i).removeRecords(columnName);
                    }
                    break;
                case ("!="):
                    if (entry.equals(conditionValue))
                    {
                        table.remove(j--);
//                        records.remove(i--);
//                        records.get(i).removeRecords(columnName);
                    }
                    break;
                default:
                    throw new DatabaseException("[Error] - "+ conditionOperator +" is invalid");
            }
        }
        System.out.println(printTable(tableName));
    }

//  Method for handling operations on ints and floats for the SELECT command
//    public ArrayList<ArrayList<String>> numberOperation() throws DatabaseException
    public void selectNumberOp() throws DatabaseException
    {
        String entry;
        float floatValue = Float.parseFloat(conditionValue);
        float floatEntry;
        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            entry = records.get(i).getColumnData(conditionColumnName);
            try {
                floatEntry = Float.parseFloat(entry);
            } catch (Exception e) {
                throw new DatabaseException("[Error] - Cannot parse a string as a number");
            }
            j = selectFloatOp(conditionOperator, floatEntry, floatValue, j);
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
