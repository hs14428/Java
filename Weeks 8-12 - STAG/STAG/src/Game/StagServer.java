package Game;

import GameExceptions.STAGException;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;

class StagServer
{
    private GameEngine stagGame;

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            stagGame = new GameEngine(entityFilename, actionFilename);
            while(true) acceptNextConnection(ss);
        } catch(IOException | ParseException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();
        try {
            out.write(stagGame.runGame(line));
        } catch (STAGException | IOException e) {
            e.printStackTrace();
            out.write(e.toString());
        }
    }
}
