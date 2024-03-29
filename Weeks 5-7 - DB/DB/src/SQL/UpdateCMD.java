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

        tableName = token;
        dbServer.setTableName(tableName);
        checkValidTable();
        token = dbServer.nextToken().toUpperCase();
        if (token.equals("SET"))
        {
            token = dbServer.nextToken();
//          Loop through and store single/multiple SET conditions. Once we reach WHERE, move on
            while (!token.toUpperCase().equals("WHERE"))
            {
                checkNameValuePair(dbServer);
                token = dbServer.nextToken();
            }
            token = token.toUpperCase();
//          Provided a WHERE follows the SET conditions, carry on. Otherwise throw a invalid token error.
            if (token.equals("WHERE"))
            {
                dbServer.decCurrentTokenNum();
                return new ConditionCMD(command).runCommand(dbServer);
            }
        }
        throw new InvalidTokenException(token);
    }

//  Note to self fix for if mix of spaces and no spaces. works for multiple conditions
    public void checkNameValuePair(DBServer dbServer) throws DatabaseException, IOException
    {
        String[] splitNameValuePair = token.split(RegEx.SPLITSET.getRegex());

        int i = 0;
        String value;
//      If no spaces between e.g. SET age=40, split and assign update values
        if (splitNameValuePair.length == 3)
        {
            columnName = splitNameValuePair[i++];
            checkValidColumn();
            if (splitNameValuePair[i++].equals("="))
            {
                value = splitNameValuePair[i];
                if (value.matches(RegEx.VALUE.getRegex()))
                {
                    updateValues.add(columnName);
                    updateValues.add(value);
                    dbServer.setUpdateValues(updateValues);
                }
            }
        }
//      If one space after =, e.g. SET age= 40, split age= and assign update values
        else if (splitNameValuePair.length == 2) {
            columnName = splitNameValuePair[i++];
            checkValidColumn();
            if (splitNameValuePair[i].equals("="))
            {
                token = dbServer.nextToken();
                value = token;
                if (value.matches(RegEx.VALUE.getRegex()))
                {
                    updateValues.add(columnName);
                    updateValues.add(value);
                    dbServer.setUpdateValues(updateValues);
                }
            }
        }
//      If there are spaces, split wont split token and token == columnName
        else {
            columnName = token;
            checkValidColumn();
            token = dbServer.nextToken();
            String[] splitNameValuePair2 = token.split(RegEx.SPLITSET.getRegex());
//          If second split length == 1, then spaces both side of = sign
            if (splitNameValuePair2.length == 1)
            {
                if (token.equals("="))
                {
                    token = dbServer.nextToken();
                    value = token;
                    if (value.matches(RegEx.VALUE.getRegex()))
                    {
                        updateValues.add(columnName);
                        updateValues.add(value);
                        dbServer.setUpdateValues(updateValues);
                    }
                }
            }
//          If got here, means input was like age =40 - split e.g. =40 and assign
            else {
                i = 0;
                if (splitNameValuePair2[i++].equals("="))
                {
                    value = splitNameValuePair2[i];
                    if (value.matches(RegEx.VALUE.getRegex()))
                    {
                        updateValues.add(columnName);
                        updateValues.add(value);
                        dbServer.setUpdateValues(updateValues);
                    }
                }
            }
        }
    }

    public void checkValidTable() throws DatabaseException, IOException
    {
        getTableNames();
        if (!tables.containsKey(tableName))
        {
            throw new DatabaseException("[ERROR] - No tables match \""+tableName+"\" input");
        }
    }

    public void getTableNames() throws DatabaseException
    {
        tables = new HashMap<String, Table>();
        tables = database.scanDBForTables();
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
        throw new DatabaseException("[ERROR] - No columns in \""+tableName+"\" table match \""+columnName+"\" input");
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
