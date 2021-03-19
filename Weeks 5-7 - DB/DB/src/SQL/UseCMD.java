package SQL;

import Database.DBServer;

public class UseCMD extends DBcmd
{
    public UseCMD()
    {
        commandType = "CommandType";
        command = "USE";
    }

    @Override
    public String runCommand(DBServer dbServer)
    {
        String token = dbServer.nextToken();
        System.out.println("Hello UseCMD class: nextToken = " + token);

        if (dbServer.previousToken().equals(command))
        {
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                databaseName = token;
                dbServer.setDatabaseName(databaseName);
                System.out.println("In UseCMD dbName = "+databaseName);
            }
        }

        return null;
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
