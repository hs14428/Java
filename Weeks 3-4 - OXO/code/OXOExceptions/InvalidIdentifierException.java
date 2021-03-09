package OXOExceptions;

public class InvalidIdentifierException extends CellDoesNotExistException
{
    public InvalidIdentifierException()
    {
    }

    public InvalidIdentifierException(int row, int column)
    {
        super(row, column);
    }

    public String toString()
    {
        return getClass().getName() + ": You have selected a cell reference with an invalid identifier. Please choose again";
    }
}
