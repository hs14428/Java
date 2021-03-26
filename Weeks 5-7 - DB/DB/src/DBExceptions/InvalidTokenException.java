package DBExceptions;

public class InvalidTokenException extends DatabaseException
{
    private String tokenGiven;

    public InvalidTokenException(String tokenGiven)
    {
        this.tokenGiven = tokenGiven;
    }

    @Override
    public String toString()
    {
        return String.format("[ERROR] - Given token: %s can not be processed. Check spelling?", tokenGiven);
    }
}
