package SQL;

import DBExceptions.DatabaseException;

public class Operator
{
    String operator;
    String value;
    boolean number;

    public Operator(String operator, String value)
    {
        this.operator = operator;
        this.value = value;
    }

    public void numberOrString() throws DatabaseException
    {
        if (value.matches(RegEx.STRINGLITERAL.getRegex()) || value.matches(RegEx.BOOLEANLITERAL.getRegex()))
        {
            number = false;
        }
        else if (value.matches(RegEx.FLOATLITERAL.getRegex()) || value.matches(RegEx.INTLITERAL.getRegex()))
        {
            number = true;
        }
        else {
            throw new DatabaseException("[Error] - Value is neither number, string or bool");
        }
    }

    public boolean isNumber() { return number ; }
}
