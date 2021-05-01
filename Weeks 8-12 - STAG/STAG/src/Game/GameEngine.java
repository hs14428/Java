package Game;

import Entities.Location;
import Entities.Player;
import GameCommands.*;
import GameExceptions.STAGException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class GameEngine
{
    private final Location startLocation;
    private Location currentLocation;
    private final LinkedHashMap<String, Location> gameMap;
    private final LinkedHashMap<String, Player> players;
    private final LinkedHashMap<String, Action> actions;
    private ArrayList<String> actionNames;
    private ArrayList<String> commands;
    private String currentPlayerName;
    private String currentCommand;
    private int commandNumber;

    public GameEngine(String entityFilename, String actionFilename) throws IOException, ParseException
    {
        GraphParser graphParser = new GraphParser(entityFilename);
        JSONParsing jsonParser = new JSONParsing(actionFilename);
        gameMap = graphParser.getGameMap();
        actions = jsonParser.getActions();
        // Set start location to first entry on gameMap, i.e. starting location
        startLocation = gameMap.entrySet().iterator().next().getValue();
        currentLocation = startLocation;
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
        checkPlayers();
        // Set current location to the current players location to allow for changing between multiple players
        currentLocation = gameMap.get(getCurrentPlayer().getPlayerLocation());
        return processCommands();
    }

    // Method checks which players are in the game. If none, add first player, if some, check if current player is new or not
    public void checkPlayers()
    {
        // Store playerNames HashMap keySet in ArrayList
        ArrayList<String> playerNames = new ArrayList<String>(players.keySet());

        // If no players yet, add one
        if (!checkForFirstPlayer(playerNames))
        {
            return;
        }
        for (String s : playerNames)
        {
            // Loop through and check if current player is new name, if so, create new player
            if (commands.get(0).equals(s))
            {
                currentPlayerName = commands.get(0);
                commandNumber++;
                return;
            }
        }
        createNewPlayer();
    }

    // Method checks if there is an initial starting player already in the game. If not add one
    public boolean checkForFirstPlayer(ArrayList<String> playerNames)
    {
        // If no players yet, add one
        if (playerNames.size() == 0)
        {
            createNewPlayer();
            return false;
        }
        return true;
    }

    public void createNewPlayer()
    {
        Player newPlayer = new Player(commands.get(0));
        newPlayer.setPlayerLocation(startLocation.getName());
        players.put(commands.get(0), newPlayer);
        currentPlayerName = commands.get(0);
        // Add the new player to start location
        startLocation.addPlayer(currentPlayerName);
        commandNumber++;
    }

    public Player getCurrentPlayer()
    {
        return players.get(currentPlayerName);
    }

    // Initiate fill Command array with the games base commands for running based off input
    public GameCommand[] initiateCommandArray()
    {
        // Could i change this into a factory?
        GameCommand[] commandType;
        GameCommand lookCMD = new LookCommand();
        GameCommand gotoCMD = new GotoCommand();
        GameCommand invCMD = new InventoryCommand();
        GameCommand getCMD = new GetCommand();
        GameCommand dropCMD = new DropCommand();
        GameCommand healthCMD = new HealthCommand();
        commandType = new GameCommand[]{lookCMD, gotoCMD, invCMD, getCMD, dropCMD, healthCMD};
        return commandType;
    }

    public String processCommands() throws STAGException
    {
        actionNames = new ArrayList<String>(actions.keySet());
        GameCommand[] commandType = initiateCommandArray();
        checkValidInput();
        commands.removeAll(Arrays.asList("", null));
        alterCommands();
        System.out.println(commands.toString());
        currentCommand = commands.get(commandNumber++);

        // Check if inputted command is a standard command type
        // Will need to add features allow for first word not being a trigger or command
        // e.g. Please open door etc.
        for (GameCommand command : commandType)
        {
            if (command.getCommandType().equalsIgnoreCase(currentCommand))
            {
                return command.runCommand(this);
            }
        }
        // Or if it is an action trigger
        return processActions();
    }

    public String processActions() throws STAGException
    {
        for (String s : actionNames)
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                return actions.get(s.toLowerCase()).performAction(this);
            }
        }
        throw new STAGException("Input command: \""+ currentCommand +"\" not recognized");
    }

    // Method to handle some common abbreviations/situations in commands, e.g. inv instead of inventory
    public void alterCommands()
    {
        int index = 0;
        for (String s : commands)
        {
            if (s.equalsIgnoreCase("inv"))
            {
                commands.set(index, "inventory");
            }
            index++;
        }
        // Cater for polite players. Start at 1 b/c first command is name
        if (commands.get(1).equalsIgnoreCase("please"))
        {
            commands.remove(1);
        }
    }

    public void checkValidInput() throws STAGException
    {
        // First check commands is not null
        if (commands.size() < 2)
        {
            throw new STAGException("Please enter a command");
        }
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

    public ArrayList<String> getCommands()
    {
        return commands;
    }

    public void setCurrentLocation(Location location)
    {
        currentLocation = location;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }
}
