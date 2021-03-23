package SQL;

import DBExceptions.DatabaseException;
import Database.DBServer;

import java.io.IOException;
import java.util.ArrayList;

public abstract class DBcmd
{
//    DBServer dbServer;
    ArrayList<ArrayList<String>> tableArrayList;
    ArrayList<String> conditions;
    ArrayList<String> columnNames;
    // Might need to save all table names in a DB down here and send back to DBSever?
    ArrayList<String> tableNames;
    String tableName;
    String databaseName;
    String commandType;
    String command;

    public DBcmd()
    {
//        this.dbServer = dbServer;
        // set all the other shit from here?
    }

    public abstract String runCommand(DBServer dbServer) throws DatabaseException, IOException;

    // Not sure if need this?
    public abstract String getCommand();
}
