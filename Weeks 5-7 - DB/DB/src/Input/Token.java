package Input;

public class Token
{
    public TokenType tokenType;
    public String token;

    public Token(String token)
    {
        this.token = token;
    }

//  Needs work
    public Token(String token, TokenType tokenType)
    {
        this.token = token;
        this.tokenType = TokenType.valueOf(token);
//        TokenType whichToken = TokenType.valueOf("use");
//        System.out.println("Tokentype: " + whichToken);
    }

    public TokenType getTokenType()
    {

        return tokenType;
    }

    public String getTokenString()
    {
        return token;
    }
}
