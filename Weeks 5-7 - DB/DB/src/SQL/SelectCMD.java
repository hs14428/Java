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
    private boolean whereClause;
    private boolean logicClause;
    private String token;

    public SelectCMD()
    {
        commandType = "CommandType";
        command = "SELECT";
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
                columnNames.add(bracketsToken.getTokenString());
            }
            dbServer.setColumnNames(columnNames);
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
                table.readTable(token);
                tableArrayList = table.getTable();
                System.out.println(tableArrayList.get(0));
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
                    dbServer.setTable(tableArrayList);
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
                tableArrayList = table.selectTable(tableName, columnNames);
                if (whereClause)
                {
                    dbServer.setTableName(tableName);
                    dbServer.setTable(tableArrayList);
                    return new ConditionCMD(command).runCommand(dbServer);
                }
                // make sure cut down table passes through with where clause
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

    @Override
    public String getCommand()
    {
        return command;
    }
}
