package DBExceptions;

public class InvalidQueryException extends DatabaseException
{
    String errorMessage;

    public InvalidQueryException()
    {
        errorMessage = "[ERROR] - Missing semi-colon at end of query or query too short.";
    }
    public InvalidQueryException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString()
    {
        return errorMessage;
    }
}
