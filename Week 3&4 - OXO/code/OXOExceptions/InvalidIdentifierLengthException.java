package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException
{
    private int length;
    private String error;

    public InvalidIdentifierLengthException()
    {
    }

    public InvalidIdentifierLengthException(int inputLength)
    {
        length = inputLength;
        if (inputLength > 2) {
            error = "long";
        }
        else {
            error = "short";
        }
    }

    public String toString()
    {
        return getClass().getName() + ": String input length is too " + error + "; " + length + " vs expected 2. Please select max a-i and 1-9.";
    }
}
