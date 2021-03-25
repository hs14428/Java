package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Table;
import Input.Token;

import java.io.IOException;
import java.util.ArrayList;

public class SelectCMD extends DBcmd
{
    private ArrayList<String> selectColumns;
    private boolean whereClause;
    private boolean logicClause;
    private String token;

    public SelectCMD()
    {
        commandType = "CommandType";
        command = "SELECT";
        selectColumns = new ArrayList<>();
        columnNames = new ArrayList<>();
        tableArrayList = new ArrayList<>();
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        scanForWhere(dbServer);
        token = dbServer.nextToken();
        databaseName = dbServer.getDatabaseName();
        System.out.println("Hello SelectCMD class: nextToken = " + token);

        if (token.equals("*"))
        {
            return selectAll(dbServer);
        }
        else if (token.matches(RegEx.BRACKETS.getRegex()))
        {
            ArrayList<Token> bracketsTokens;
            bracketsTokens = dbServer.getBrackets(token);
            for (Token bracketsToken : bracketsTokens)
            {
                selectColumns.add(bracketsToken.getTokenString());
            }
//            dbServer.setColumnNames(selectColumns);
            return selectSome(dbServer);
        }
        else {
            throw new InvalidTokenException(token);
        }
    }

    public String selectAll(DBServer dbServer) throws DatabaseException, IOException
    {
        token = dbServer.nextToken().toUpperCase();
        Table table = new Table(databaseName);

        if (token.equals("FROM"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                tableName = token;
                getColumnNames(tableName);
                dbServer.setColumnNames(columnNames);
                System.out.println("select *: "+columnNames);
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
                    return new ConditionCMD(command).runCommand(dbServer);
                }
                table.readTable(tableName);
                String printTable = table.printTable(tableName);
                return "[OK]\n"+printTable;
            }
        }
        throw new InvalidTokenException(token);
    }

    public String selectSome(DBServer dbServer) throws DatabaseException, IOException
    {
        token = dbServer.nextToken().toUpperCase();
        Table table = new Table(databaseName);

        if (token.equals("FROM"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                tableName = token;
                checkValidColumn();
                columnNames = selectColumns;
                dbServer.setColumnNames(columnNames);
                System.out.println("selectSome: "+columnNames);
//                tableArrayList = table.selectTable(tableName, columnNames);
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
//                    dbServer.setTable(tableArrayList);
                    return new ConditionCMD(command).runCommand(dbServer);
                }
                tableArrayList = table.readTable(tableName);
                table.trimTable(tableArrayList, columnNames);
                String printTable = table.printTable(tableName);
                return "[OK]\n"+printTable;
            }
        }
        throw new InvalidTokenException(token);
    }

    public void scanForWhere(DBServer dbServer) throws DatabaseException
    {
        whereClause = false;
        int startTokenNum = dbServer.getCurrentTokenNum();
        for (int i = startTokenNum; i < dbServer.getQueryLength()-1; i++)
        {
            token = dbServer.nextToken().toUpperCase();
            if (token.equals("WHERE"))
            {
                whereClause = true;
            }
        }
        dbServer.setCurrentTokenNum(startTokenNum);
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

    public void checkValidColumn() throws DatabaseException, IOException
    {
        System.out.println("table "+tableName);
        System.out.println("columns "+selectColumns);

        getColumnNames(tableName);
        int validColumns = 0;
        for (String selectColumn : selectColumns)
        {
            for (String columnName : columnNames)
            {
                if (selectColumn.equals(columnName))
                {
                    validColumns++;
                }
            }
        }
        if (!(validColumns == selectColumns.size()))
        {
            throw new DatabaseException("[Error] - Incorrect columns given. "+selectColumns+" do not match "+columnNames+" columns from "+tableName+" table");
        }
    }

    public void getColumnNames(String tableName) throws DatabaseException, IOException
    {
        System.out.println("db "+databaseName);
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
