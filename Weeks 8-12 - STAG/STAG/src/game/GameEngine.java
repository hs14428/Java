package game;

import GameCommands.*;
import GameExceptions.STAGException;

import java.io.IOException;
import java.util.*;

public class GameEngine
{
    private GraphParser graphParser;
    private Location currentLocation;
    private LinkedHashMap<String, Location> gameMap;
    private LinkedHashMap<String, Player> players;
    private ArrayList<String> commands;
    private String currentPlayerName;
    private String currentCommand;
    private String errorMessage;
    int commandNumber;

    public GameEngine(String entityFilename, String actionFilename)
    {
        graphParser = new GraphParser(entityFilename);
        gameMap = graphParser.getGameMap();
        // Set start location to first entry on gameMap, i.e. starting location
        currentLocation = gameMap.entrySet().iterator().next().getValue();
        System.out.println(currentLocation.getName()+" " +currentLocation.getDescription());
        players = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, Location> getGameMap()
    {
        return gameMap;
    }

    public String runGame(String incomingCommand) throws IOException, STAGException
    {
        incomingCommand = incomingCommand.replace(":","");
        String[] commandArray = incomingCommand.split(" ");
        commands = new ArrayList<String>(Arrays.asList(commandArray));
        commandNumber = 0;
        checkPlayer();

        return processCommands();
    }

    public void checkPlayer()
    {
        // Store playerNames HashMap in set
        Set<String> playerNames = players.keySet();

        // If no players yet, add one
        if (playerNames.size() == 0)
        {
            Player player = new Player(commands.get(0));
            players.put(commands.get(0), player);
        }

        for (String s : playerNames)
        {
            // Loop through and check if current player is new name, if so, create new player
            if (!commands.get(0).equals(s))
            {
                Player player = new Player(commands.get(0));
                players.put(commands.get(0), player);
            }
        }
        currentPlayerName = commands.get(0);
        commandNumber++;
    }

    public Player getCurrentPlayer()
    {
        return players.get(currentPlayerName);
    }

    public String processCommands() throws STAGException
    {
        GameCommand[] commandType;
        GameCommand lookCMD = new LookCommand();
        GameCommand gotoCMD = new GotoCommand();
        // Will need to deal with inv later
        GameCommand invCMD = new InventoryCommand();
        GameCommand getCMD = new GetCommand();
        GameCommand dropCMD = new DropCommand();
        commandType = new GameCommand[]{lookCMD, gotoCMD, invCMD, getCMD, dropCMD};
        currentCommand = commands.get(commandNumber++);
        System.out.println("pc: "+currentCommand);
        System.out.println("pC: "+commandNumber);
        for (GameCommand command : commandType)
        {
            if (command.getCommandType().equals(currentCommand))
            {
                return command.runCommand(this);
            }
        }
        throw new STAGException("Input command: \""+currentCommand+"\" not recognized");
    }

    public String getCurrentCommand()
    {
        return currentCommand;
    }

    public String getNextCommand() throws STAGException
    {
        if (commandNumber < commands.size())
        {
            commandNumber = commandNumber++;
            return commands.get(commandNumber);
        }
        throw new STAGException("Not any commands left to process.");
    }

    public void setCurrentLocation(Location location)
    {
        currentLocation = location;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }
//    public String processCommands() throws STAGException, IOException
//    {
//        currentCommand = commands.get(commandNumber++);
//        if (currentCommand.equalsIgnoreCase("look"))
//        {
//            return lookCommand();
//        }
//        else if (currentCommand.equalsIgnoreCase("goto"))
//        {
//            currentCommand = commands.get(commandNumber);
//            return gotoCommand();
//        }
//        else if (currentCommand.equalsIgnoreCase("get"))
//        {
//            return null;
//        }
//        else {
//            throw new STAGException("Input command: \""+currentCommand+"\" not recognized");
//        }
//    }
//
//    public String gotoCommand() throws STAGException
//    {
//        for (String s : currentLocation.getPaths())
//        {
//            if (currentCommand.equalsIgnoreCase(s))
//            {
//                currentLocation = gameMap.get(currentCommand);
//                return lookCommand();
//            }
//        }
//        errorMessage = "Invalid path selected. Valid Paths: ";
//        errorMessage += currentLocation.getPathsString();
//        throw new STAGException(errorMessage);
//    }
//
//    public String lookCommand()
//    {
//        String gameState = "";
//
//        gameState += currentLocation.getDescription();
//        gameState += ". You can see:\n";
//        gameState += currentLocation.getEntityDescriptions();
//        gameState += "You can access from here:\n";
//        gameState += currentLocation.getPathsString();
//        System.out.println(gameState);
//        return gameState;
//    }

}
