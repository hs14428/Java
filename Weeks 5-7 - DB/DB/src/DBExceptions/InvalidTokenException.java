package DBExceptions;

import Input.Token;

public class InvalidTokenException
{
    private Token tokenGiven;
    private Token tokenExpected;

    public InvalidTokenException(Token tokenGiven, Token tokenExpected)
    {
        this.tokenGiven = tokenGiven;
        this.tokenExpected = tokenExpected;
    }

    @Override
    public String toString() {
        return String.format("Given token: %s, does not match expected token: %s",
                tokenGiven.toString(), tokenExpected.toString());
    }
}
