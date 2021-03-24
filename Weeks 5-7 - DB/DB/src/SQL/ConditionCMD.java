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
        tableArrayList = dbServer.getTable();
        System.out.println(tableArrayList.get(0));
        getColumnNames(tableName);

        System.out.println("Hello ConditionCMD class: nextToken = " + token);

//      Check for AND/OR in a query and if so call AND/OR recursive condition

//      Otherwise call basic where condition
        System.out.println(token+" before where");
        if (token.equals("WHERE"))
        {
            System.out.println("After WHERE check");
            token = dbServer.nextToken();
            System.out.println(token+" before columnName loop");
            for (String columnName : columnNames)
            {
                System.out.println(token+" = "+columnName+" before columnName check");
                if (token.equals(columnName))
                {
                    System.out.println("After checking columnName is valid");
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
        System.out.println(token+" in storeCondition");
        conditions.add(token);
        System.out.println(conditions+" in storeCondition");
        token = dbServer.nextToken();
        System.out.println(token+" before operator match");
        if (token.matches(RegEx.OPERATOR.getRegex()))
        {
            // Add operator to conditions
            conditions.add(token);
            System.out.println(conditions+" in operator match");
            token = dbServer.nextToken();
            System.out.println(token+" in operator match");
            if (token.matches(RegEx.VALUE.getRegex()))
            {
                // Add value to conditions
                conditions.add(token);
                System.out.println(conditions+" in value match");
                System.out.println("command before switch: "+command);
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

    public String updateCondition(DBServer dbServer)
    {

        return null;
    }

    public String deleteCondition(DBServer dbServer)
    {

        return null;
    }

    public String selectCondition(DBServer dbServer) throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        System.out.println("we made it into processCondition");

        if (conditions.get(conditionNum).matches(RegEx.ANDOR.getRegex()))
        {
            //do something
            System.out.println("check not in ANDOR");
            conditionNum++;
        }
        System.out.println("before condition table shit");
        System.out.println("conditionNum before entering CT: " + conditionNum);
        System.out.println(tableArrayList.get(0));
        tableArrayList = table.conditionTable(tableArrayList, conditions, conditionNum);
        String printTable = table.printTable(tableName);
        System.out.println("after CT shit");
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
