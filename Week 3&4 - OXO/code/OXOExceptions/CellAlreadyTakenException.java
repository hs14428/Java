package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException
{
    private final int asciiA = 97;

    public CellAlreadyTakenException()
    {
    }

    public CellAlreadyTakenException(int row, int column)
    {
        super(row, column);
    }

    public String toString()
    {
        return getClass().getName() + ": " +(char)(getRow()+asciiA) + (getColumn()+1) +" is already taken. Please select another cell.";
    }

}
