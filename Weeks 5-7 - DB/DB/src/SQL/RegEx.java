package SQL;

public enum RegEx
{
    VARIABLENAME ("[a-zA-Z0-9_.]+"),
    BRACKETS ("\\(\\s*(.*)\\s*\\)"),
    STRINGLITERAL ("'[a-zA-Z0-9_.-@ ]+'"),
    BOOLEANLITERAL ("true|false"),
    FLOATLITERAL ("[0-9]+.[0-9]+"),
    INTLITERAL ("[0-9]+"),
    VALUE (STRINGLITERAL.getRegex()+"|"+BOOLEANLITERAL.getRegex()+"|"+FLOATLITERAL.getRegex()+"|"+INTLITERAL.getRegex()),
    OPERATOR ("\\s*==\\s*|\\s*>\\s*|\\s*<\\s*|\\s*>=\\s*|\\s*<=\\s*|\\s*!=\\s*|\\s*LIKE\\s*"),
    WHERESPLIT ("((?<===)|(?===))|((?<=>[^=])|(?=>[^=]))|((?<=<[^=])|(?=<[^=]))|((?<=>=)|(?=>=))|((?<=<=)|(?=<=))|((?<=LIKE)|(?=LIKE))"),
    SPLITSET ("((?<==)|(?==))"),
    ANDOR ("AND|OR");


    String regexPattern;

    RegEx(String regex)
    {
        regexPattern = regex;
    }

    public String getRegex()
    {
        return regexPattern;
    }
}
