package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    private final int asciiA = 97;
    private char character;
    private RowOrColumn type;

    public InvalidIdentifierCharacterException()
    {
    }

    public InvalidIdentifierCharacterException(int row, int column, RowOrColumn inputType)
    {
        super(row, column);
        type = inputType;
        character = (char) (row+asciiA);
    }

    public String toString()
    {
        return getClass().getName() + ": "+ character + (getColumn()+1) +" is and invalid cell because "+ character +" is an invalid "+ type +" selection. Please select a different "+ type +" character.";
    }
}
