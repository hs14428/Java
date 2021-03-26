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
    private String leftTableName;
    private String rightTableName;
    private String leftJoinColumn;
    private String rightJoinColumn;
    private String tableName;
    private String databaseName;
    private String databasePath;
    private String currentDirectory;
    private String extension;
    private Database database;

    private Operator op;
    private String conditionColumnName;
    private String conditionOperator;
    private String conditionValue;

    private ArrayList<ArrayList<String>> table;
    private ArrayList<Record> records;
    private Record record;


//  tableName is really just the .tab file name and database is really just the directory/folder
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
            throw new TableException("[ERROR] - "+tableName +" does not exist.");
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

//  Add the contents of a records hashmap back into an ArrayList<ArrayList<String>> table
    public void recordsToTable() throws IOException
    {
        ArrayList<ArrayList<String>> newTable = new ArrayList<>();
        ArrayList<String> columnNames = readColumnNames(tableName);
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
                throw new TableException("[ERROR] - Cannot find the path specified");
            }

            FileWriter writer = new FileWriter(newTable);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("id");
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        }
        else {
            throw new TableException("[ERROR] - Table: "+tableName+" of same name already exists");
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
            throw new TableException("[ERROR] - Table: "+tableName+" does not exist");
        }
    }

//  Save down contents of table into file on users computer
    public void writeToTable(String tableName) throws IOException
    {
        File writeToTable = new File(databasePath + File.separator + tableName + extension);

        if (!writeToTable.exists())
        {
            throw new TableException("[ERROR] - Table: "+tableName+" does not exists. Failed to write");
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

//  Add columns to the table and save down
    public void addColumns(String tableName, ArrayList<Token> columnNames) throws IOException, InvalidTokenException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);

        if (tableToAddTo.exists())
        {
            for (Token columnName : columnNames)
            {
//              Check the contents of the columnNames arraylist are valid
                if (!columnName.getTokenString().matches(RegEx.VARIABLENAME.getRegex()))
                {
                    throw new InvalidTokenException(columnName.getTokenString());
                }
//              Add column name to end of table columns
                table.get(0).add(columnName.getTokenString());
//              Go down each row and add an empty string at the end of each entry for new column
                for (int i = 1; i < table.size(); i++)
                {
                    table.get(i).add(" ");
                }
//              Write new table back out to .tab file
                writeToTable(tableName);
            }
        }
        else {
            throw new TableException("[ERROR] - Table: "+tableName+" does not exist.");
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
            table.get(0).remove(tableColumnIndex);
//          Go down each row and add an empty string at the end of each entry for new column
            for (int i = 1; i < table.size(); i++)
            {
                table.get(i).remove(tableColumnIndex);
            }
//          Write new table back out to .tab file
            writeToTable(tableName);
        }
        else {
            throw new TableException("[ERROR] - Table: "+tableName+" does not exist.");
        }
    }

    public void addRow(String tableName, ArrayList<String> rowValues) throws IOException
    {
        File tableToAddTo = new File(databasePath + File.separator + tableName + extension);
        table = readTable(tableName);
        addRecords();
        String dynamicId;
        int intDynamicId;

//      Check size of records to see if its empty. If so, set entry id = 1, otherwise add one to the current max entry id
        if (records.size() == 0)
        {
            intDynamicId = 1;
        }
        else {
            dynamicId = records.get(records.size()-1).getColumnData("id");
            intDynamicId = Integer.parseInt(dynamicId);
            intDynamicId += 1;
        }

        String newIdNum = String.valueOf(intDynamicId);

        if (tableToAddTo.exists())
        {
            rowValues.add(0, newIdNum);
            table.add(rowValues);
            writeToTable(tableName);
        }
        else {
            throw new TableException("[ERROR] - Table: "+tableName+" does not exist.");
        }
    }

    public String printTable()
    {
        String tableString = "";
        for (ArrayList<String> strings : table)
        {
            for (String string : strings)
            {
                tableString += string.replace("'","");
                tableString += "\t";
            }
            tableString += "\n";
        }
        return tableString;
    }

//  Method to trim down a table and remove any columns that have been chosen not to be printed
    public ArrayList<ArrayList<String>> trimTable(ArrayList<ArrayList<String>> table, ArrayList<String> columnNames)
    {
        this.table = table;
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

//  Method for saving down the tables column names into an ArrayList<String>
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

//  Method for joining together tables based on inputs
    public String joinTables(String tableNameL, String tableNameR, String colNameL, String colNameR) throws IOException
    {
        leftTableName = tableNameL;
        rightTableName = tableNameR;
        leftJoinColumn = colNameL;
        rightJoinColumn = colNameR;
        ArrayList<ArrayList<String>> joinedTable = new ArrayList<>();
        ArrayList<ArrayList<String>> leftTable = new ArrayList<>();
        ArrayList<ArrayList<String>> rightTable = new ArrayList<>();
        ArrayList<Record> leftRecords = new ArrayList<Record>();
        ArrayList<Record> rightRecords = new ArrayList<Record>();

        leftTable = readTable(leftTableName);
        addRecords();
        leftRecords = records;
        joinedTable = readTable(leftTableName);
        rightTable = readTable(rightTableName);
        addRecords();
        rightRecords = records;

        joinedTable = changeColumnNames(joinedTable, leftTableName);

        String leftColumnValue;
        String rightColumnValue;
        String dataToJoin;
        int matchCount;
        int newColumnSize = leftTable.get(0).size() + rightTable.get(0).size()-1;

        for (int i = 0; i < leftRecords.size(); i++)
        {
            leftColumnValue = leftRecords.get(i).getColumnData(leftJoinColumn);
            matchCount = 0;
            for (int j = 0; j < rightRecords.size(); j++)
            {
                rightColumnValue = rightRecords.get(j).getColumnData(rightJoinColumn);
                if (rightColumnValue.equals(leftColumnValue))
                {
                    matchCount++;
                    if (matchCount > 1)
                    {
                        joinedTable.add(leftTable.get(i+1));
                    }
                    for (int k = 1; k < rightTable.get(j).size(); k++)
                    {
                        dataToJoin = rightTable.get(j+1).get(k);
                        if (joinedTable.get(i+1).size()+1 > newColumnSize)
                        {
                            joinedTable.get(i+matchCount).add(dataToJoin);
                        } else {
                            joinedTable.get(i+1).add(dataToJoin);
                        }
                    }
                }
            }
        }
        changeColumnNames(rightTable, rightTableName);
        rightTable.get(0).remove(0);
        joinedTable.get(0).addAll(rightTable.get(0));
        for (int i = 1; i < joinedTable.size(); i++)
        {
            String intString = String.valueOf(i);
            joinedTable.get(i).set(0, intString);
        }
        table = joinedTable;
        return printTable();
    }

    public ArrayList<ArrayList<String>> changeColumnNames(ArrayList<ArrayList<String>> tableToChange, String appendString)
    {
        String oldColumnName;
        for (int i = 1; i < tableToChange.get(0).size(); i++)
        {
            oldColumnName = tableToChange.get(0).get(i);
            tableToChange.get(0).set(i, appendString+"."+oldColumnName);
        }
        return tableToChange;
    }

//  Method to save duplicate lines. Sets up condition columnName, Operator and Value
    public void setUpConditionVars(String tableName, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        table = readTable(tableName);
        this.tableName = tableName;
        addRecords();
        conditionColumnName = conditions.get(conditionNum++);
        conditionOperator = conditions.get(conditionNum++);
        conditionValue = conditions.get(conditionNum++);
        op = new Operator(conditionOperator, conditionValue);
//      Check if column being operator on is a String/Bool or Int/Float type column and then
//      perform the correct operation on it
        op.valueNumberOrString();
    }

//  Delete rows from table based on a WHERE condition. Use of conditionNum allow for multiple conditions to be processed
    public void deleteRowsFromTable(String tableName, ArrayList<String> conditions, int conditionNum) throws IOException, DatabaseException
    {

        setUpConditionVars(tableName, conditions, conditionNum);

        if (op.isValueNumber())
        {
            deleteNumberOp();
        }
        else if (!op.isValueNumber())
        {
            deleteStringBoolOp();
        }
        else {
            throw new DatabaseException("[ERROR] - Could not update table. Check inputs.");
        }
    }

//  Method which is used for when deleting rows from table when the condition is a number, i.e. age == 40
    private void deleteNumberOp() throws DatabaseException, IOException
    {
        float floatCondValue = Float.parseFloat(conditionValue);
        Operator op = new Operator();

        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            float floatCondColumnData = Float.parseFloat(records.get(i).getColumnData(conditionColumnName));
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
                case (">="):
                    if (op.floatGreaterThanEqual(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                case ("<="):
                    if (op.floatLessThanEqual(floatCondColumnData, floatCondValue))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                default:
                    throw new DatabaseException("[ERROR] - Error deleting from the table.");
            }
        }
    }

//  Method which is used for when deleting rows from table when the condition is a String or Bool, i.e. name == 'Clive'
    private void deleteStringBoolOp() throws DatabaseException, IOException
    {
        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
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
                case ("LIKE"):
                    String trimmedEntry = conditionValue.replace("'","");
                    if (records.get(i).getColumnData(conditionColumnName).contains(trimmedEntry))
                    {
                        deleteTableValues(i--);
                    }
                    break;
                default:
                    throw new DatabaseException("[ERROR] - Error deleting from the table.");
            }
        }
    }

//  Method to reduce repeat lines of code that removes rows of records, converts to a table and saves to file system
    private void deleteTableValues(int i) throws IOException
    {
        records.remove(i);
        recordsToTable();
        writeToTable(tableName);
    }

//  Update values in a table based on a WHERE condition. Use of conditionNum allow for multiple conditions to be processed
    public void updateTable(String tableName, ArrayList<String> updateValues, ArrayList<String> conditions, int conditionNum) throws IOException, DatabaseException
    {

        setUpConditionVars(tableName, conditions, conditionNum);

        if (op.isValueNumber())
        {
            updateNumberOp(updateValues);
        }
        else if (!op.isValueNumber())
        {
            updateStringBoolOp(updateValues);
        }
        else {
            throw new DatabaseException("[ERROR] - Could not update table. Check inputs.");
        }
    }

//  Method which is used for when updating rows from table when the condition is a number, i.e. age == 40
    public void updateNumberOp(ArrayList<String> updateValues) throws DatabaseException, IOException
    {
        float floatCondValue = Float.parseFloat(conditionValue);
        Operator op = new Operator();

//      While loop for if there are multiple values to update. Will loop until all the values have been set
//      Values from updateValues are removed once implemented so size will fall to 0
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
                        if (op.floatGreaterThan(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case("<"):
                        if (op.floatLessThan(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case (">="):
                        if (op.floatGreaterThanEqual(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    case ("<="):
                        if (op.floatLessThanEqual(floatCondColumnData, floatCondValue))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    default:
                        throw new DatabaseException("[ERROR] - Error updating the table.");
                }
            }
//          Remove the update value pairs and "bring forward" the next pair (if there is more)
            updateValues.remove(0);
            updateValues.remove(0);
        }
    }

//  Method which is used for when updating rows from table when the condition is a String or Bool, i.e. name == 'Clive' or pass == true
    private void updateStringBoolOp(ArrayList<String> updateValues) throws DatabaseException, IOException
    {
//      While loop for if there are multiple values to update. Will loop until all the values have been set
//      Values from updateValues are removed once implemented so size will fall to 0
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
                    case ("LIKE"):
                        String trimmedEntry = conditionValue.replace("'","");
                        if (records.get(i).getColumnData(conditionColumnName).contains(trimmedEntry))
                        {
                            updateTableValues(i, updateColumnName, updateValue);
                        }
                        break;
                    default:
                        throw new DatabaseException("[ERROR] - Error updating the table.");
                }
            }
// R        emove the update value pairs and "bring forward" the next pair (if there is more)
            updateValues.remove(0);
            updateValues.remove(0);
        }
    }

//  Method that actually implements the updating of values. Replaces existing hashmap row value for column with new data
    private void updateTableValues(int i, String updateColumnName, String updateValue) throws IOException
    {
        records.get(i).addToRecords(updateColumnName, updateValue);
        recordsToTable();
        writeToTable(tableName);
    }

    //  Method used for the SELECT command to return cut down table with less columns based on the WHERE conditions

    public String selectTable(String tableName, ArrayList<String> columnNames, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        setUpConditionVars(tableName, conditions, conditionNum);

        if (op.isValueNumber())
        {
            selectNumberOp();
            trimTable(table, columnNames);
            return printTable();
        }
        if (!op.isValueNumber())
        {
            selectStringBoolOp();
            trimTable(table, columnNames);
            return printTable();
        }
        throw new DatabaseException("[ERROR] - Could not carry out select WHERE clause. Check inputs.");
    }

//  Method used for the SELECT command to return cut down table with less columns based on the WHERE conditions
    public ArrayList<ArrayList<String>> selectAndTable(ArrayList<ArrayList<String>> inputTable, ArrayList<String> columnNames, ArrayList<String> conditions, int conditionNum) throws DatabaseException, IOException
    {
        table = inputTable;
        addRecords();

        conditionColumnName = conditions.get(conditionNum++);
        conditionOperator = conditions.get(conditionNum++);
        conditionValue = conditions.get(conditionNum++);
        Operator op = new Operator(conditionOperator, conditionValue);
        op.valueNumberOrString();

        if (op.isValueNumber())
        {
            selectNumberOp();
            trimTable(table, columnNames);
            return table;
        }
        if (!op.isValueNumber())
        {
            selectStringBoolOp();
            trimTable(table, columnNames);
            return table;
        }
        throw new DatabaseException("[ERROR] - Could not carry out select WHERE clause. Check inputs.");
    }

//  Method for operating on Strings and Bools for the SELECT command
    public void selectStringBoolOp() throws DatabaseException
    {
        String entry;
        int j = 1;
        for (int i = 0; i < records.size(); i++, j++)
        {
            entry = records.get(i).getColumnData(conditionColumnName);

            switch (conditionOperator)
            {
                case ("=="):
                    if (!entry.equals(conditionValue))
                    {
                        table.remove(j--);
                    }
                    break;
                case ("!="):
                    if (entry.equals(conditionValue))
                    {
                        table.remove(j--);
                    }
                    break;
                case ("LIKE"):
                    String trimmedEntry = conditionValue.replace("'","");
                    if (!entry.contains(trimmedEntry))
                    {
                        table.remove(j--);
                    }
                    break;
                default:
                    throw new DatabaseException("[ERROR] - "+ conditionOperator +" is invalid");
            }
        }
    }

//  Method for handling operations on ints and floats for the SELECT command
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
                throw new DatabaseException("[ERROR] - Cannot parse a string as a number");
            }
            j = selectFloatOp(conditionOperator, floatEntry, floatValue, j);
        }
    }

//  Method for comparing floats and removing rows of table based on operator for the SELECT command
    public int selectFloatOp(String operator, Float entry, Float value, int j) throws DatabaseException
    {
        int index = j;
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
            case (">="):
                if (!op.floatGreaterThanEqual(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            case ("<="):
                if (!op.floatLessThanEqual(entry, value))
                {
                    table.remove(index--);
                }
                return index;
            default:
                throw new DatabaseException("[ERROR] - "+operator+" is invalid");
        }
    }

    public ArrayList<ArrayList<String>> getTable() { return table ; }
}
