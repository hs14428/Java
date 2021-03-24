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

    public ConditionCMD(String command)
    {
        this.command = command;
        conditions = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        token = dbServer.nextToken().toUpperCase();
        databaseName = dbServer.getDatabaseName();
        tableName = dbServer.getTableName();
        columnNames = dbServer.getColumnNames();
        getColumnNames(tableName);
        System.out.println("Hello ConditionCMD class: nextToken = " + token);

//      Check for AND/OR in a query and if so call AND/OR recursive condition
//      Otherwise call basic where condition
        if (token.equals("WHERE"))
        {
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

    public String storeCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        // Add column name to conditions
        conditions.add(token);
        token = dbServer.nextToken();
        if (token.matches(RegEx.OPERATOR.getRegex()))
        {
            // Add operator to conditions
            conditions.add(token);
            token = dbServer.nextToken();
            if (token.matches(RegEx.VALUE.getRegex()))
            {
                // Add value to conditions
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
                        throw new DatabaseException("[Error] - Missing condition type command.");
                }
            }
        }
        throw new InvalidTokenException(token);
    }

    public String updateCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        System.out.println("hello, you made it");
        System.out.println(conditions.get(0));
        System.out.println(conditions.get(1));
        System.out.println(conditions.get(2));

        Table table = new Table(databaseName);
        updateValues = dbServer.getUpdateValues();
        System.out.println(tableName);
        table.updateTable(tableName, updateValues, conditions, conditionNum);
        return "[OK] - Values updated";
    }

    public String deleteCondition(DBServer dbServer)
    {

        return null;
    }

    public String selectCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        // If this breaks i moved this from the runCommand method
        tableArrayList = dbServer.getTable();
        Table table = new Table(databaseName);

        if (conditions.get(conditionNum).matches(RegEx.ANDOR.getRegex()))
        {
            //do something
            System.out.println("check not in ANDOR");
            conditionNum++;
        }
        String printTable  = table.conditionTable(tableArrayList, conditions, conditionNum);
        return "[OK]\n" + printTable;
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnNames = new ArrayList<>();
        columnNames = table.readColumnNames(tableName);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
