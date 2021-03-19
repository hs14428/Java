package SQL;

import DBExceptions.DatabaseException;
import Database.DBServer;

import java.util.List;

public abstract class DBcmd
{
//    DBServer dbServer;
    List<Condition> conditions;
    List<String> columnNames;
    List<String> tableNames;
    String databaseName;
    String commandType;
    String command;

    public DBcmd()
    {
//        this.dbServer = dbServer;
        // set all the other shit from here?
    }

    public abstract String runCommand(DBServer dbServer) throws DatabaseException;

    public abstract String getCommand();
}
