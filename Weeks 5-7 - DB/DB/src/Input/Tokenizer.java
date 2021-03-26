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
    private ArrayList<Token> bracketsArrayList;
    private Token token;
    private String command;
    private int currentToken;
    private boolean whereClause;

    public Tokenizer(String incomingCommand)
    {
        command = incomingCommand;
        currentToken = 0;
    }

//  Apologies for the bootleg fix here - I know it's not pretty, but I realised an error late on
//  and time was against me!
//    public ArrayList<Token> tokenize() throws InvalidQueryException
//    {
//        String[] tokenArray = command.split(",*\\s+(?![^(]*\\))");
//
//        // Currently doesnt work for Name== 'Harry'; Age< 42; etc
//        tokenArray = tokenizeCondition(tokenArray);
//
//        for (String value : tokenArray)
//        {
//            System.out.println(value);
//        }
//
//        int endTokenIndex = tokenArray.length-1;
//        String endToken = tokenArray[endTokenIndex];
//        if (!checkValidEnd(endToken))
//        {
//            System.out.println("Tokenizer tokenize() error.");
//            throw new InvalidQueryException();
//        }
//        tokenArray[endTokenIndex] = endToken.replace(";","");
//        tokenArrayList = new ArrayList<>();
//        for (String s : tokenArray)
//        {
//            token = new Token(s);
//            tokenArrayList.add(token);
//        }
//        return tokenArrayList;
//    }
    public ArrayList<Token> tokenize() throws InvalidQueryException
    {
        // First check for only semi colon at end (and remove it) and if query is long enough
        command = checkValidEnd2(command);

        String[] tokenArray = initialTokenSplit(command);
        System.out.println("pre condition");
        for (String value : tokenArray)
        {
            System.out.println(value);
        }

        // Currently doesnt work for Name== 'Harry'; Age< 42; etc
        tokenArray = tokenizeCondition(tokenArray);

        System.out.println("post condition");
        for (String value : tokenArray)
        {
            System.out.println(value);
        }

        tokenArrayList = new ArrayList<>();
        for (String s : tokenArray)
        {
            token = new Token(s);
            tokenArrayList.add(token);
        }
        return tokenArrayList;
    }

    public void scanForWhere(String query)
    {
        whereClause = false;
//        Pattern p = Pattern.compile()
    }

    public String[] initialTokenSplit(String command)
    {
        Pattern pattern = Pattern.compile("\\(.*?\\)|'.*?'|[^('\\s]+");
        String[] tokenArray = pattern.matcher(command).results().map(matchResult -> matchResult.group()).toArray(String[]::new);
//        for (String s : tokenArray)
//        {
//            System.out.println(s);
//        }
        return tokenArray;
    }

    public String checkValidEnd2(String s) throws InvalidQueryException
    {
        int finalChar = s.length()-1;
        int penultimateChar = s.length()-2;

        if ((s == null) || ( s.length() == 0))
        {
            throw new InvalidQueryException("[Error] - Input string is too short");
        }
        if (s.substring(finalChar).equals(";"))
        {
            if (!s.substring(penultimateChar, finalChar).equals(";"))
            {
                s = s.substring(0, finalChar);
                System.out.println(s);
                return s;
            }
        }
        throw new InvalidQueryException("[Error] - Missing semi colon in query string OR Too many semi colons");
    }

//  split out conditions with operators without spaces STILL NEED TO FIX >= <= operators
    public String[] tokenizeCondition(String[] tokenArray)
    {
        String[] operatorSplit;
        for (int i=0; i<tokenArray.length;i++)
        {
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

    public boolean checkValidEnd(String endToken)
    {
        if (endToken.substring(endToken.length()-1).equals(";"))
        {
            return true;
        }
        return false;
    }

//  Will need to add a getToken method for this?
    public ArrayList<Token> tokenizeBrackets(String tokenBrackets) throws InvalidQueryException
    {
        bracketsArrayList = new ArrayList<>();
        String[] bracketsArray = tokenBrackets.substring(1,tokenBrackets.length()-1).split(",");

        if (bracketsArray.length == 1)
        {
            System.out.println("Brackets tokenize() error.");
            throw new InvalidQueryException();
        }

        for (String s : bracketsArray) {
            token = new Token(s.trim());
            bracketsArrayList.add(token);
        }
        return bracketsArrayList;
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
