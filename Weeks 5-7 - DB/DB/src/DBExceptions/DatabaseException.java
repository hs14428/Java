package DBExceptions;

public class DatabaseException extends Exception
{
    String errorMessage;

    public DatabaseException()
    {

    }

    public DatabaseException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString()
    {
        return errorMessage;
    }
}
