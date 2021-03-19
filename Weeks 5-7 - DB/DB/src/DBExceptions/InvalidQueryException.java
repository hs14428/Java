package DBExceptions;

public class InvalidQueryException extends DatabaseException
{

    public InvalidQueryException()
    {
    }

    @Override
    public String toString()
    {
        return "[ERROR] - Missing semi-colon at end of query or query too short.";
    }
}
