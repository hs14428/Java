package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    private int col;
    private int position;
    private RowOrColumn type;
    private final int asciiA = 97;

    public OutsideCellRangeException()
    {
    }

    public OutsideCellRangeException(int inputRow, int inputColumn, RowOrColumn inputType)
    {
        super(inputRow, inputColumn);
        position = inputRow + asciiA;
        col = inputColumn+1;
        type = inputType;
    }

    public String toString()
    {
        return getClass().getName() + ": This cell, " + (char)position + col + ", is out of the current " + type + " limits. Please select another cell.";
    }
}
