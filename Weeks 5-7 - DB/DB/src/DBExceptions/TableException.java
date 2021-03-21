package DBExceptions;

import java.io.IOException;

public class TableException extends IOException
{
    String errorMessage;

    public TableException()
    {

    }

    public TableException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString()
    {
        return errorMessage;
    }
}
