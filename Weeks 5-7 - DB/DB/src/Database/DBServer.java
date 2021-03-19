package Database;

import DBExceptions.DatabaseException;
import Input.Parser;
import Input.Token;
import Input.Tokenizer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DBServer
{
    private ArrayList<Token> tokenList = new ArrayList<>();
    private Tokenizer tokenizer;
    private String databaseName;
    private int currentTokenNum;

    public DBServer(int portNumber)
    {
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
            System.out.println("Try 1");
            tokenizer = new Tokenizer(incomingCommand);
            System.out.println("Try 2");
            tokenList = tokenizer.tokenize();
            System.out.println("Try 3");
            currentTokenNum = 0;
            System.out.println("Try 4");
            Parser parser = new Parser(this);
            System.out.println("Try 5");
            socketWriter.write(parser.parse().runCommand(this));
        } catch (DatabaseException e) {
            System.out.println("In Catch");
            e.printStackTrace();
            socketWriter.write(e.toString());
        }
        System.out.println("Rest of Program");
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }

    public Tokenizer getTokenizer()
    {
        return tokenizer;
    }

    public ArrayList<Token> getTokens()
    {
        return tokenList;
    }

    public String nextToken()
    {
        incCurrentTokenNum();
        String token = tokenList.get(currentTokenNum).getTokenString();
        return token;
    }

    public String previousToken()
    {
        String previousToken = tokenList.get(currentTokenNum-1).getTokenString();
        return previousToken;
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

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

//    public String getOriginalCommand()
//    {
//        return originalCommand;
//    }

}
