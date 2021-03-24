package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCMD extends DBcmd
{
    Database database;
    String token;
    String columnName;
    private ArrayList<String> updateValues;

    public UpdateCMD()
    {
        commandType = "CommandType";
        command = "UPDATE";
        updateValues = new ArrayList<String>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        databaseName = dbServer.getDatabaseName();
        database = new Database(databaseName);
        token = dbServer.nextToken();
        System.out.println("Hello UpdateCMD class: nextToken = " + token);

        if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            tableName = token;
            dbServer.setTableName(tableName);
            checkValidTable();
            System.out.println("after checkValidTable");
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("SET"))
            {
                token = dbServer.nextToken();
                checkNameValuePair(dbServer);
//                columnName = token;
//                checkValidColumn();
                token = dbServer.nextToken().toUpperCase();
                if (token.equals("WHERE"))
                {
//                    Table table = new Table(databaseName);
//                    tableArrayList = table.getTable();
                    dbServer.decCurrentTokenNum();
                    return new ConditionCMD(command).runCommand(dbServer);
                }
            }
        }
        throw new InvalidTokenException(token);
    }
//  Note to self check for multiple sets (stretch goal)
    public void checkNameValuePair(DBServer dbServer) throws DatabaseException, IOException
    {
        System.out.println("in checkNameValuePair");
        System.out.println(dbServer.getCurrentTokenNum());
        int index = dbServer.getCurrentTokenNum();
        System.out.println(token);
        String[] splitNameValuePair = token.split(RegEx.SPLITSET.getRegex());

        int i = 0;
        String value;
        if (splitNameValuePair.length == 3)
        {
            System.out.println(splitNameValuePair[0]);
            System.out.println(splitNameValuePair[1]);
            System.out.println(splitNameValuePair[2]);
            columnName = splitNameValuePair[i++];
            System.out.println("columnName "+columnName);
            checkValidColumn();
            if (splitNameValuePair[i++].equals("="))
            {
                value = splitNameValuePair[i];
                if (value.matches(RegEx.VALUE.getRegex()))
                {
                    System.out.println(value);
                    System.out.println(columnName);
                    updateValues.add(columnName);
                    updateValues.add(value);
                    dbServer.setUpdateValues(updateValues);
                }
            }
        }
        else {
            columnName = token;
            System.out.println(columnName);
            checkValidColumn();
            token = dbServer.nextToken();
            System.out.println("line 95: "+token);
            if (token.equals("="))
            {
                token = dbServer.nextToken();
                System.out.println("line 99: "+token);
                value = token;
                if (value.matches(RegEx.VALUE.getRegex()))
                {
                    System.out.println(value);
                    System.out.println(columnName);
                    updateValues.add(columnName);
                    updateValues.add(value);
                    dbServer.setUpdateValues(updateValues);
                }
            }
        }
    }

    public void checkValidTable() throws DatabaseException, IOException
    {
        getTableNames();
        if (!tables.containsKey(tableName))
        {
            throw new DatabaseException("[Error] - No tables match \""+tableName+"\" input");
        }
    }

    public void checkValidColumn() throws DatabaseException, IOException
    {
        getColumnNames(tableName);

        for (String name : columnNames)
        {
            if (name.equals(columnName))
            {
                return;
            }
        }
        throw new DatabaseException("[Error] - No columns in \""+tableName+"\" table match \""+columnName+"\" input");
    }

    public void getTableNames() throws DatabaseException, IOException
    {
        tables = new HashMap<String, Table>();
        tables = database.scanDBForTables();
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnNames = new ArrayList<>();
        columnNames = table.readColumnNames(tableName);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
