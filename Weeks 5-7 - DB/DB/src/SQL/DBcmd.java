package SQL;

import DBExceptions.DatabaseException;
import Database.DBServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DBcmd
{
//    DBServer dbServer;
    List<Condition> conditions;
    List<String> columnNames;
    // Might need to save all table names in a DB down here and send back to DBSever?
    List<String> tableNames;
    String databaseName;
    String commandType;
    String command;

    public DBcmd()
    {
        tableNames = new ArrayList<>();
//        this.dbServer = dbServer;
        // set all the other shit from here?
    }

    public abstract String runCommand(DBServer dbServer) throws DatabaseException, IOException;

    public abstract String getCommand();
}
