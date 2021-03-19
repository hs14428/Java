package DBExceptions;

import java.io.IOException;

public class EmptyCommandException extends IOException
{
//    String errorMessage;

    public EmptyCommandException()
    {
//    errorMessage = message;
    }

    @Override
    public String toString() {
        return String.format("Error with input command.");
    }
}
