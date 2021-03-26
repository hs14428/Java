package Input;

import DBExceptions.InvalidQueryException;
import SQL.RegEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer
{
    private ArrayList<Token> tokenArrayList;
    private Token token;
    private String command;
    private int currentToken;

    public Tokenizer(String incomingCommand)
    {
        command = incomingCommand;
        currentToken = 0;
    }

    public ArrayList<Token> tokenize() throws InvalidQueryException
    {
//      First check for semi colon at end (and remove it) and if query is long enough
        command = checkValidEnd(command);

        String[] tokenArray = initialTokenSplit(command);
//      If there is a singular WHERE condition, correctly split it further
        tokenArray = tokenizeCondition(tokenArray);
        tokenArrayList = new ArrayList<>();
        for (String s : tokenArray)
        {
            token = new Token(s);
            tokenArrayList.add(token);
        }
        return tokenArrayList;
    }

    public String[] initialTokenSplit(String command)
    {
        Pattern pattern = Pattern.compile("\\(.*?\\)|'.*?'|[^('\\s]+");
        String[] tokenArray = pattern.matcher(command).results().map(matchResult -> matchResult.group()).toArray(String[]::new);

        return tokenArray;
    }

//  Check that the string query ends with a semi colon and is long enough/not empty
    public String checkValidEnd(String s) throws InvalidQueryException
    {
        int finalChar = s.length()-1;
        int penultimateChar = s.length()-2;

        if ((s == null) || ( s.length() == 0))
        {
            throw new InvalidQueryException("[ERROR] - Input string is too short");
        }
        if (s.substring(finalChar).equals(";"))
        {
            if (!s.substring(penultimateChar, finalChar).equals(";"))
            {
                s = s.substring(0, finalChar);
                return s;
            }
        }
        throw new InvalidQueryException("[ERROR] - Missing semi colon in query string OR Too many semi colons");
    }

//  Split out conditions with operators without spaces
    public String[] tokenizeCondition(String[] tokenArray)
    {
        String[] operatorSplit;
        for (int i=0; i<tokenArray.length;i++)
        {
//          Check for operators in string and if so, split it and add to the end of tokenArray
            Pattern p = Pattern.compile(RegEx.WHERESPLIT.getRegex());
            Matcher m = p.matcher(tokenArray[i]);
            if (m.find())
            {
                operatorSplit = tokenArray[i].split(RegEx.WHERESPLIT.getRegex());
                tokenArray[i++] = operatorSplit[0];
                tokenArray = copyArray(i, tokenArray, operatorSplit);
                // kill loop to save time
                i = tokenArray.length;
            }
        }
        return tokenArray;
    }

//  Used for copying split conditions onto the end of the main tokenArray
    private String[] copyArray(int i, String[] tokenArray, String[] operatorSplit)
    {
        int arraySize = tokenArray.length + operatorSplit.length-1;
        tokenArray = Arrays.copyOf(tokenArray, arraySize);
        for (int j = 1; j < operatorSplit.length; j++)
        {
            String holdEndToken = tokenArray[i];
            tokenArray[i++] = operatorSplit[j];
            tokenArray[i] = holdEndToken;
        }
        return tokenArray;
    }

//  Brackets are originally split out grouped in first token split. Now split for processing during the interpreting
    public ArrayList<Token> tokenizeBrackets(String tokenBrackets) throws InvalidQueryException
    {
        ArrayList<Token> bracketsArrayList = new ArrayList<>();
        String[] bracketsArray = tokenBrackets.substring(1,tokenBrackets.length()-1).split(",");

        if (bracketsArray.length == 1)
        {
            throw new InvalidQueryException();
        }

        for (String s : bracketsArray) {
            token = new Token(s.trim());
            bracketsArrayList.add(token);
        }
        return bracketsArrayList;
    }

//  Get the next token
    public Token nextToken()
    {
        token = tokenArrayList.get(currentToken++);
        return token;
    }

}
