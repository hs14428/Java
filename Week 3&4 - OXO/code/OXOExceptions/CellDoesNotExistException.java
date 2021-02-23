package OXOExceptions;

public class CellDoesNotExistException extends OXOMoveException
{
    public CellDoesNotExistException ()
    {
    }

    public CellDoesNotExistException (int row, int column)
    {
        super(row, column);
    }

    public String toString()
    {
        return "Error with " + getClass().getName() + ": " + getRow()+getColumn() + " is an invalid cell. Your selection might be of bounds, or an incorrect cell identifier chosen.";
    }
}
