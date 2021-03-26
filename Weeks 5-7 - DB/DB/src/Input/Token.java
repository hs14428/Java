package Input;

public class Token
{
    private TokenType tokenType;
    private String token;

    public Token(String token)
    {
        this.token = token;
    }

    public Token(String token, TokenType tokenType)
    {
        this.token = token;
        this.tokenType = TokenType.valueOf(token);
    }

    public TokenType getTokenType()
    {

        return tokenType;
    }

    public String getTokenString()
    {
        return token;
    }

    public void setTokenString(String newToken)
    {
        token = newToken;
    }
}
