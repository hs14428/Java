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

        if (token.equals("*"))
        {
            return selectAll(dbServer);
        }
        else if (token.matches(RegEx.VARIABLENAME.getRegex()))
        {
//          If singular column, add to select columns
            selectColumns.add(token);
            return selectSome(dbServer);
        }
        else if (token.matches(RegEx.BRACKETS.getRegex()))
        {
//          If multiple columns, build list to chose from later
            ArrayList<Token> bracketsTokens;
            bracketsTokens = dbServer.getBrackets(token);
            for (Token bracketsToken : bracketsTokens)
            {
                selectColumns.add(bracketsToken.getTokenString());
            }
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
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
                    return new ConditionCMD(command).runCommand(dbServer);
                }
//              Check that you are at last token. If not query is invalid
                checkFinalToken(dbServer);
                table.readTable(tableName);
                String printTable = table.printTable();
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
//              Check if the chosen selectColumns from runCommand are valid/in table
                checkValidColumn();
                columnNames = selectColumns;
                dbServer.setColumnNames(columnNames);
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
                    return new ConditionCMD(command).runCommand(dbServer);
                }
//              Check that you are at last token. If not query is invalid
                checkFinalToken(dbServer);
                tableArrayList = table.readTable(tableName);
                table.trimTable(tableArrayList, columnNames);
                String printTable = table.printTable();
                return "[OK]\n"+printTable;
            }
        }
        throw new InvalidTokenException(token);
    }

    public void checkFinalToken(DBServer dbServer) throws DatabaseException
    {
        if (!(dbServer.getCurrentTokenNum() == dbServer.getQueryLength()-1))
        {
            throw new DatabaseException("[ERROR] - Invalid Query. Missing WHERE keyword");
        }
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
            throw new DatabaseException("[ERROR] - Incorrect columns given. "+selectColumns+" do not match "+columnNames+" columns from "+tableName+" table");
        }
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
