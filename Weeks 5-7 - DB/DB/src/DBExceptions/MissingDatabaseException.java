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
        return String.format("Database %s does not exist.", databaseName);
    }
}
