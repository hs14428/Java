package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException
{
    public CellAlreadyTakenException()
    {
    }

    public CellAlreadyTakenException(int row, int column)
    {
        super(row, column);
    }

    public String toString()
    {
        return getClass().getName() + ": This cell is already taken. Please select another cell.";
    }

}
