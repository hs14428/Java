package DBExceptions;

public class ConditionException extends Exception
{
    String errorMessage;

    public ConditionException()
    {

    }

    public ConditionException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString()
    {
        return errorMessage;
    }
}
