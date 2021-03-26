package DBExceptions;

public class MissingDatabaseException extends DatabaseException
{
    String databaseName;

    public MissingDatabaseException(String token)
    {
        databaseName = token;
    }

    @Override
    public String toString()
    {
        return String.format("[ERROR] - %s database does not exist.", databaseName);
    }
}
