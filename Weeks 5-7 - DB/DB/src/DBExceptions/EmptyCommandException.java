package DBExceptions;

public class EmptyCommandException extends DatabaseException
{
//    String errorMessage;

    public EmptyCommandException()
    {
//    errorMessage = message;
    }

    @Override
    public String toString()
    {
        return "[Error] - Input command appears to be too short.";
    }
}
