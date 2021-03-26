package Database;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidQueryException;
import Input.Parser;
import Input.Token;
import Input.Tokenizer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DBServer
{
    private ArrayList<ArrayList<String>> table = new ArrayList<>();
    private ArrayList<Token> tokenList = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<String> updateValues = new ArrayList<>();
    private Tokenizer tokenizer;
    private String currentDirectory;
    private String baseDirectory;
    private String databaseName;
    private String tableName;
    private int currentTokenNum;

    public DBServer(int portNumber)
    {
        createDBEnvironment();
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException
    {
        String incomingCommand = socketReader.readLine();
        System.out.println("Received message: " + incomingCommand);
        try {
            tokenizer = new Tokenizer(incomingCommand);
            tokenList = tokenizer.tokenize();
            currentTokenNum = 0;
            Parser parser = new Parser(this);
            socketWriter.write(parser.parse().runCommand(this));
        } catch (DatabaseException | IOException e) {
            e.printStackTrace();
            socketWriter.write(e.toString());
        }
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }

    public void createDBEnvironment()
    {
        currentDirectory = ".";
        baseDirectory = "Databases";
        File database = new File(currentDirectory + File.separator + baseDirectory);

        if (!database.exists())
        {
            database.mkdirs();
        }
    }

    public ArrayList<Token> getTokens()
    {
        return tokenList;
    }

    public ArrayList<Token> getBrackets(String tokenBrackets) throws InvalidQueryException
    {
        ArrayList<Token> bracketsList;
        bracketsList = tokenizer.tokenizeBrackets(tokenBrackets);
        return bracketsList;
    }

    public String nextToken() throws DatabaseException
    {
        String token;
        incCurrentTokenNum();
        if (getCurrentTokenNum() < getQueryLength())
        {
            token = tokenList.get(currentTokenNum).getTokenString();
            return token;
        }
        throw new DatabaseException("[Error] - Run out of tokens");
    }

    public int getCurrentTokenNum()
    {
        return currentTokenNum;
    }

    public void setCurrentTokenNum(int index)
    {
        currentTokenNum = index;
    }

    public void incCurrentTokenNum()
    {
        currentTokenNum++;
    }

    public void decCurrentTokenNum()
    {
        currentTokenNum--;
    }

    public int getQueryLength()
    {
        return tokenList.size();
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTable(ArrayList<ArrayList<String>> table) { this.table = table; }

    public ArrayList<ArrayList<String>> getTable() { return table; }

    public void setColumnNames(ArrayList<String> columnNames)
    {
        this.columnNames = columnNames;
    }

    public ArrayList<String> getColumnNames()
    {
        return columnNames;
    }

    public void setUpdateValues(ArrayList<String> updateValues)
    {
        this.updateValues = updateValues;
    }

    public ArrayList<String> getUpdateValues()
    {
        return updateValues;
    }
}
