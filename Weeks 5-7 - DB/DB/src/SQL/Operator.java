package SQL;

import DBExceptions.DatabaseException;

public class Operator
{
    String operator;
    String value;
    boolean numberBool;

    public Operator()
    {

    }

    public Operator(String operator, String value)
    {
        this.operator = operator;
        this.value = value;
    }

    public void valueNumberOrString() throws DatabaseException
    {
        if (value.matches(RegEx.STRINGLITERAL.getRegex()) || value.matches(RegEx.BOOLEANLITERAL.getRegex()))
        {
            numberBool = false;
        }
        else if (value.matches(RegEx.FLOATLITERAL.getRegex()) || value.matches(RegEx.INTLITERAL.getRegex()))
        {
            numberBool = true;
        }
        else {
            throw new DatabaseException("[ERROR] - Value is neither number, string or bool");
        }
    }

    public boolean floatEqualsTo(Float entry, Float value)
    {
        return entry.compareTo(value) == 0;
    }

    public boolean floatGreaterThan(Float entry, Float value)
    {
        if (entry.compareTo(value) < 0)
        {
            return false;
        }
        return entry.compareTo(value) != 0;
    }

    public boolean floatLessThan(Float entry, Float value)
    {
        if (entry.compareTo(value) > 0)
        {
            return false;
        }
        return entry.compareTo(value) != 0;
    }

    public boolean floatGreaterThanEqual(Float entry, Float value)
    {
        return (entry.compareTo(value) >= 0) || (entry.compareTo(value) == 0);
    }

    public boolean floatLessThanEqual(Float entry, Float value)
    {
        return (entry.compareTo(value) <= 0) || (entry.compareTo(value) == 0);
    }

    public boolean isValueNumber() { return numberBool; }
}
