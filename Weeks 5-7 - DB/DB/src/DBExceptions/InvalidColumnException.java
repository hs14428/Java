package DBExceptions;

public class InvalidColumnException extends DatabaseException
{
    private String tokenGiven;

    public InvalidColumnException(String tokenGiven)
    {
        this.tokenGiven = tokenGiven;
    }

    @Override
    public String toString()
    {
        return String.format("[ERROR] - Given token: %s is an invalid column", tokenGiven);
    }
}
