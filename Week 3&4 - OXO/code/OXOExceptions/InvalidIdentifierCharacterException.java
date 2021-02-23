package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    private char character;
    private RowOrColumn type;

    public InvalidIdentifierCharacterException()
    {
    }

    public InvalidIdentifierCharacterException(int row, int column, char inputChar, RowOrColumn inputType)
    {
        super(row, column);
        character = inputChar;
        type = inputType;
    }

    public String toString()
    {
        return "Error with " + this.getClass().getName() + ": This input is invalid. Character " + character + " is an invalid selection. Please select another row reference.";
    }
}
