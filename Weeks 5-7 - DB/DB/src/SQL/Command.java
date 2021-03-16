package SQL;
import Database.Database;

import java.util.*;

public abstract class Command
{
    List<Condition> conditions;
    List<String> columnNames;
    List<String> tableNames;
    String databaseName;
    String commandType;
    String command;

    public Command(String command)
    {
        this.command = command;
    }

    public abstract String runCommand(Database database);
}
