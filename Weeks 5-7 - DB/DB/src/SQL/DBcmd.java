package SQL;

import DBExceptions.DatabaseException;
import Database.DBServer;
import Database.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DBcmd
{
    ArrayList<String> updateValues;
    ArrayList<ArrayList<String>> tableArrayList;
    HashMap<String, Table> tables;
    ArrayList<String> conditions;
    ArrayList<String> columnNames;
    ArrayList<String> tableNames;
    String tableName;
    String databaseName;
    String commandType;
    String command;

    public DBcmd()
    {
    }

    public abstract String runCommand(DBServer dbServer) throws DatabaseException, IOException;

    // Not sure if need this?
    public abstract String getCommand();
}
