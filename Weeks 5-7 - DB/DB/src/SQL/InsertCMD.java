package SQL;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidTokenException;
import Database.DBServer;

import java.io.IOException;

public class InsertCMD extends DBcmd
{

    public InsertCMD()
    {
        commandType = "CommandType";
        command = "INSERT";
    }

    @Override
    public String runCommand(DBServer dbServer) throws DatabaseException, IOException
    {
        String token = dbServer.nextToken().toUpperCase();
        System.out.println("Hello InsertCMD class: nextToken = " + token);

        if (token.equals("INTO"))
        {
            token = dbServer.nextToken();
            if (token.matches(RegEx.VARIABLENAME.getRegex()))
            {
                token = dbServer.nextToken();
                if (token.equals("VALUES"))
                {

                }
            }
            return "[OK] - Values inserted";
        }
        throw new InvalidTokenException(token);

    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
