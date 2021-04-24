package game;

import GameCommands.*;
import GameExceptions.STAGException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class GameEngine
{
    private GraphParser graphParser;
    private JSONParsing jsonParser;
    private Location currentLocation;
    private Location startLocation;
    private LinkedHashMap<String, Location> gameMap;
    private LinkedHashMap<String, Player> players;
    private LinkedHashMap<String, Action> actions;
    private ArrayList<String> commands;
    private String currentPlayerName;
    private String currentCommand;
    private String errorMessage;
    int commandNumber;

    public GameEngine(String entityFilename, String actionFilename) throws IOException, ParseException
    {
        graphParser = new GraphParser(entityFilename);
        jsonParser = new JSONParsing(actionFilename);
        gameMap = graphParser.getGameMap();
        actions = jsonParser.getActions();
        // Set start location to first entry on gameMap, i.e. starting location
        startLocation = gameMap.entrySet().iterator().next().getValue();
        currentLocation = startLocation;
        System.out.println(currentLocation.getName()+" " +currentLocation.getDescription());
        players = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, Location> getGameMap()
    {
        return gameMap;
    }

    public Location getStartLocation()
    {
        return startLocation;
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
        // Could i change this into a factory?
        GameCommand lookCMD = new LookCommand();
        GameCommand gotoCMD = new GotoCommand();
        // Will need to deal with inv later
        GameCommand invCMD = new InventoryCommand();
        GameCommand getCMD = new GetCommand();
        GameCommand dropCMD = new DropCommand();
        GameCommand healthCMD = new HealthCommand();
        commandType = new GameCommand[]{lookCMD, gotoCMD, invCMD, getCMD, dropCMD, healthCMD};
        Set<String> actionNames = actions.keySet();
        currentCommand = commands.get(commandNumber++);

        // Check if inputted command is a standard command type
        for (GameCommand command : commandType)
        {
            if (command.getCommandType().equals(currentCommand))
            {
                return command.runCommand(this);
            }
        }
        // Or if it is an action trigger
        for (String s : actionNames)
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                return actions.get(s).performAction(this);
            }
        }
        throw new STAGException("Input command: \""+currentCommand+"\" not recognized");
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
