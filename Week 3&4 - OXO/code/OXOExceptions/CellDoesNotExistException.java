package OXOExceptions;

public class CellDoesNotExistException extends OXOMoveException
{
    private final int asciiA = 97;

    public CellDoesNotExistException ()
    {
    }

    public CellDoesNotExistException (int inputRow, int inputColumn)
    {
        super(inputRow, inputColumn);
    }

    public String toString()
    {
        return getClass().getName() + ": " + (char)(getRow()+asciiA) + (getColumn()+1) + " is an invalid cell. Your selection is out of the max 9x9 board size.";
    }
}
