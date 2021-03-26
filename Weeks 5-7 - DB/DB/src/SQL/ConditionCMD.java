package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidColumnException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Table;

import java.io.IOException;
import java.util.ArrayList;

public class ConditionCMD extends DBcmd
{
    String token;
    int conditionNum;
    private boolean logicClause;

    public ConditionCMD(String command)
    {
        this.command = command;
        conditions = new ArrayList<>();
        conditionNum = 0;
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        scanForLogic(dbServer);
        token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();
        tableName = dbServer.getTableName();
        columnNames = dbServer.getColumnNames();
        getColumnNames(tableName);

//      Check for AND/OR in a query and if so call AND/OR recursive condition
//      Otherwise call basic where condition
        if (token.equals("WHERE"))
        {
            if (logicClause)
            {
                removeBrackets(dbServer);
                return storeMultiConditions(dbServer);
            }
            token = dbServer.nextToken();
            for (String columnName : columnNames)
            {
                if (token.equals(columnName))
                {
                    return storeCondition(dbServer);
                }
            }
            throw new InvalidColumnException(token);
        }
        throw new InvalidTokenException(token);
    }

    public boolean checkValidColumn(String column) throws DatabaseException
    {
        for (String columnName : columnNames)
        {
            System.out.println(columnNames);
            System.out.println(column);
            if (column.equals(columnName))
            {
                System.out.println("true");
                return true;
            }
        }
        throw new DatabaseException("[ERROR] - Incorrect columns given. ");
    }

    public String storeCondition(DBServer dbServer) throws DatabaseException, IOException
    {
//      Need to add some kind of catch/loop for multiple conditions with AND OR
//      Add column name to conditions
        conditions.add(token);
        token = dbServer.nextToken();
        if (token.matches(RegEx.OPERATOR.getRegex()))
        {
//          Add operator to conditions
            conditions.add(token);
            token = dbServer.nextToken();
            if (token.matches(RegEx.VALUE.getRegex()))
            {
//              Add value to conditions
                conditions.add(token);
                switch(command)
                {
                    case ("SELECT"):
                        return selectCondition(dbServer);
                    case ("UPDATE"):
                        return updateCondition(dbServer);
                    case ("DELETE"):
                        return deleteCondition(dbServer);
                    default:
                        throw new DatabaseException("[ERROR] - Missing condition type command.");
                }
            }
        }
        throw new InvalidTokenException(token);
    }

    public String updateCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        updateValues = dbServer.getUpdateValues();
        table.updateTable(tableName, updateValues, conditions, conditionNum);
        return "[OK] - Values updated";
    }

    public String deleteCondition(DBServer dbServer) throws IOException, DatabaseException
    {
        Table table = new Table(databaseName);
        table.deleteRowsFromTable(tableName, conditions, conditionNum);
        return "[OK] - Values deleted";
    }

    public String selectCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        tableArrayList = dbServer.getTable();
        columnNames = dbServer.getColumnNames();
        Table table = new Table(databaseName);
        String printTable;

        if (logicClause)
        {
            // Only works for AND... Sorry for how crap this is...
            tableArrayList = table.readTable(tableName);
            while (conditions.size() != 0)
            {
                if (conditions.get(0).equals("AND"))
                {
                    conditions.remove(0);
                }
                tableArrayList = table.selectAndTable(tableArrayList, columnNames, conditions, conditionNum);
                conditions.remove(0);
                conditions.remove(0);
                conditions.remove(0);
            }
            printTable = table.printTable();
        } else {
            printTable  = table.selectTable(tableName, columnNames, conditions, conditionNum);
        }
        return "[OK]\n" + printTable;
    }

    private String storeMultiConditions(DBServer dbServer) throws DatabaseException, IOException
    {
        String[] conditionsArray;
        while ((dbServer.getCurrentTokenNum() != dbServer.getQueryLength()-1))
        {
            token = dbServer.nextToken();
            conditionsArray = token.split(RegEx.WHERESPLIT.getRegex());
            for (int i = 0; i < conditionsArray.length; i++)
            {
                conditionsArray[i] = conditionsArray[i].trim();
//              Check if each part of the condition matches allowed input
                if (conditionsArray[i].matches(RegEx.OPERATOR.getRegex()) || conditionsArray[i].matches(RegEx.ANDOR.getRegex())
                        || conditionsArray[i].matches(RegEx.VALUE.getRegex()) || checkValidColumn(conditionsArray[i]))
                {
                    conditions.add(conditionsArray[i].trim());
                }
            }
        }
        switch(command)
        {
            case ("SELECT"):
                return selectCondition(dbServer);
            case ("UPDATE"):
                return updateCondition(dbServer);
            case ("DELETE"):
                return deleteCondition(dbServer);
            default:
                throw new DatabaseException("[ERROR] - Missing condition type command.");
        }
    }

    private void removeBrackets(DBServer dbServer) throws DatabaseException
    {
        int startTokenNum = dbServer.getCurrentTokenNum();
        int endTokenNum = dbServer.getQueryLength();
        for (int i = startTokenNum+1; i < endTokenNum; i++)
        {
            token = dbServer.nextToken();
            if (token.contains("(") && token.contains(")"))
            {
                token = token.replace("(","");
                token = token.replace(")","");
                dbServer.replaceToken(token);
            }
        }
        dbServer.setCurrentTokenNum(startTokenNum);
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnNames = new ArrayList<>();
        columnNames = table.readColumnNames(tableName);
    }

    public void scanForLogic(DBServer dbServer) throws DatabaseException
    {
        logicClause = false;
        int startTokenNum = dbServer.getCurrentTokenNum();
        for (int i = startTokenNum; i < dbServer.getQueryLength()-1; i++)
        {
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("AND") || token.equals("OR"))
            {
                logicClause = true;
            }
        }
        dbServer.setCurrentTokenNum(startTokenNum);
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
