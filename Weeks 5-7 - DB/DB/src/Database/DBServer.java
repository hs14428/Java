package Database;

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
            tokenizer = new Tokenizer(incomingCommand);
            tokenList = tokenizer.tokenize();
            currentTokenNum = 0;
            Parser parser = new Parser(this);
            parser.parse().runCommand(this);
        } catch (IOException e) {
            System.out.println("Breaking in processNextCommand");
            e.printStackTrace();
            socketWriter.write(e.getMessage());
        }
        socketWriter.write("[OK] Thanks for your message: " + incomingCommand);
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
