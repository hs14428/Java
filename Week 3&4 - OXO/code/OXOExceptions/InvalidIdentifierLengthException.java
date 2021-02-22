package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException
{
    private int length;

    public InvalidIdentifierLengthException()
    {
    }

    public InvalidIdentifierLengthException(int row, int column, int inputLength)
    {
        super(row, column);
        length = inputLength;
    }

    public String toString()
    {
        return "Error with " + getClass().getName() + ": This input is invalid. Length of" + length + ". Please select another cell of style x(1-9).";
    }
}
