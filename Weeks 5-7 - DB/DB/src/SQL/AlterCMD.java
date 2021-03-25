package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;
import Database.Database;
import Database.Table;
import Input.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AlterCMD extends DBcmd
{
    String token;
    Database database;
    String columnName;
    ArrayList<Token> columnTokens;

    public AlterCMD()
    {
        commandType = "CommandType";
        command = "ALTER";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        databaseName = dbServer.getDatabaseName();
        System.out.println("database "+databaseName);
        token = dbServer.nextToken().toUpperCase();
        System.out.println("Hello AlterCMD class: nextToken = " + token);

        if (token.equals("TABLE"))
        {
            token = dbServer.nextToken();
            tableName = token;
            dbServer.setTableName(tableName);
            checkValidTable();
            token = dbServer.nextToken().toUpperCase();
            System.out.println("token after checkvalidtable"+token);
            if (token.equals("ADD"))
            {
                System.out.println("in add check");
                token = dbServer.nextToken();
                System.out.println(token);
                return addCommand();
            }
            if (token.equals("DROP"))
            {
                token = dbServer.nextToken();
                System.out.println(token);
                return dropCommand();
            }
        }
        throw new InvalidTokenException(token);
    }

    public String addCommand() throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnTokens = new ArrayList<>();
        if (!token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            throw new InvalidTokenException(token);
        }
        // Maybe update to remove legacy Token class which wasnt really used?
        columnName = token;
        Token columnToken = new Token(columnName);
        columnTokens.add(columnToken);
        table.addColumns(tableName, columnTokens);
        System.out.println("after add columns");

        return "[OK] - Column added";
    }

    public String dropCommand() throws DatabaseException, IOException
    {
        Table table = new Table(databaseName);
        columnTokens = new ArrayList<>();
        if (!token.matches(RegEx.VARIABLENAME.getRegex()))
        {
            throw new InvalidTokenException(token);
        }
        columnName = token;
        table.removeColumn(tableName, columnName);

        return "[OK] - Column dropped";
    }

    public void checkValidTable() throws DatabaseException
    {
        getTableNames();
        if (!tables.containsKey(tableName))
        {
            throw new DatabaseException("[Error] - No tables match \""+tableName+"\" input");
        }
        System.out.println("passed check valid table");
    }

    public void getTableNames() throws DatabaseException
    {
        database = new Database(databaseName);
        tables = new HashMap<String, Table>();
        tables = database.scanDBForTables();
    }

    @Override
    public String getCommand()
    {
        return command;
    }

}
