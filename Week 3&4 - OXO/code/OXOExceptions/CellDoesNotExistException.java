package OXOExceptions;

public class CellDoesNotExistException extends OXOMoveException
{
    private final int asciiA = 97;
    private final int ascii1 = 49;

    public CellDoesNotExistException ()
    {
    }

    public CellDoesNotExistException (int inputRow, int inputColumn)
    {
        super(inputRow, inputColumn);
    }

    public String toString()
    {
        return getClass().getName() + ": " + (char)(getRow()+asciiA) + (char)(getColumn()+ascii1) +
                " is an invalid cell. Your selection does not fit the cell reference style for a max 9x9 board size (a-i + 1-9).";
    }
}
