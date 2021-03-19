package SQL;

public enum RegEx
{
    VARIABLENAME ("[a-zA-Z0-9_.]+");

    String pattern;

    RegEx(String regex)
    {
        pattern = regex;
    }

    public String getRegex()
    {
        return pattern;
    }
}
