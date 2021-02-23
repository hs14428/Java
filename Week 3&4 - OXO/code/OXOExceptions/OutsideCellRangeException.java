package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    private int position;
    private RowOrColumn type;

    public OutsideCellRangeException()
    {
    }

    public OutsideCellRangeException(int row, int column, int inputPosition, RowOrColumn inputType)
    {
        super(row, column);
        position = inputPosition;
        type = inputType;
    }

    public String toString()
    {
        return getClass().getName() + ": This cell, " + (char) position + ", is out of the current " + type + " limits. Please select another cell.";
    }
}
