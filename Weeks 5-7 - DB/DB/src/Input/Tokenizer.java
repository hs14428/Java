package Input;

import DBExceptions.InvalidQueryException;

import java.util.ArrayList;

public class Tokenizer
{
    private ArrayList<Token> tokenArrayList;
    private ArrayList<Token> bracketsArrayList;
    private Token token;
    private String command;
    private int currentToken;
    private int finalToken;

    public Tokenizer(String incomingCommand)
    {
        command = incomingCommand;
        currentToken = 0;
        finalToken = 0;
    }

//  name='clive' doesnt need spaces --> will need to check strings char by char to split here
//  Or include = into split regex
    public ArrayList<Token> tokenize() throws InvalidQueryException
    {
        String[] tokenArray = command.split("\\s+(?![^(]*\\))");
        int endTokenIndex = tokenArray.length-1;
        String endToken = tokenArray[endTokenIndex];

        if (tokenArray.length == 1 || !checkValidEnd(endToken))
        {
            System.out.println("Tokenizer tokenize() error.");
            throw new InvalidQueryException();
        }
        tokenArray[endTokenIndex] = endToken.replace(";","");
        tokenArrayList = new ArrayList<>();
        for (String s : tokenArray) {
            token = new Token(s);
            tokenArrayList.add(token);
            finalToken++;
        }
        return tokenArrayList;
    }

    public boolean checkValidEnd(String endToken)
    {
        if (endToken.substring(endToken.length()-1).equals(";"))
        {
            return true;
        }
        return false;
    }

//  Will need to add a getToken method for this?
    public void tokenizeBrackets(String tokenBrackets)
    {
        bracketsArrayList = new ArrayList<>();
        String[] bracketsArray = tokenBrackets.substring(1,tokenBrackets.length()-1).split(",");
        for (String s : bracketsArray) {
            token = new Token(s.trim());
            bracketsArrayList.add(token);
        }
    }

//  Might not need if in Token class?
    public Token getToken(int index)
    {
        return tokenArrayList.get(index);
    }

    public Token nextToken()
    {
        token = tokenArrayList.get(currentToken++);
        return token;
    }

    public void setCurrentTokenIndex(int index)
    {
        currentToken = index;
    }

    public int getCurrentTokenIndex()
    {
        return currentToken;
    }

}
