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
    private final ArrayList<Action> actionArrayList;
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
        actionArrayList = jsonParser.getActionsAL();
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
        // Remove the colon from the player name in prep for storing inputted commands
        incomingCommand = incomingCommand.replace(":","");
        // Split incoming commands on spaces and add to arraylist
        String[] commandArray = incomingCommand.split(" ");
        commands = new ArrayList<String>(Arrays.asList(commandArray));
        commandNumber = 0;
        // Check if current player is first player in game, if so add new player, otherwise check current player against current players to see if need to create new player
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

        // Check if the first player is in the game, if so loop through and check if current player is new or not
        if (checkForFirstPlayer(playerNames))
        {
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

    public void storeActionNames()
    {
        String actionName;
        actionNames = new ArrayList<String>();
        for (Action a : actionArrayList)
        {
            actionName = a.getActionName();
            actionNames.add(actionName);
        }
    }

    public String processCommands() throws STAGException
    {
        storeActionNames();
        GameCommand[] commandType = initiateCommandArray();
        // Check that the inputted commands isn't empty, bar name
        checkValidInput();
        // Remove any array entries that are empty, i.e. if there was double+ spaces in input
        commands.removeAll(Arrays.asList("", null));
        // Make minor amendments to the commands, i.e. accept inv as well as inventory
        alterCommands();
        System.out.println(commands.toString());
        currentCommand = commands.get(commandNumber++);

        // Check if inputted command is a standard command type
        for (GameCommand command : commandType)
        {
            // Can ignore case because commands are built in
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
        int index = 0;
        for (String s : actionNames)
        {
            // Case sensitive for actions
            if (currentCommand.equals(s))
            {
                // Check that performing the action is actually possible in current gamestate/location
                if (actionArrayList.get(index).performAction(this))
                {
                    // If so, return relevant game update to player
                    return actionArrayList.get(index).getNarration();
                }
            }
            index++;
        }
        throw new STAGException("Input not recognized, check your spelling");
    }

    // Method to handle some common abbreviations/situations in commands, e.g. inv instead of inventory
    public void alterCommands()
    {
        // Cater for polite players. Start at 1 b/c first command is name
        // Checking for if input starts with please and removes that entry from commands ArrayList
        if (commands.get(1).equalsIgnoreCase("please"))
        {
            commands.remove(1);
        }
        // Allow for inv or inventory for the command
        if (commands.get(1).equals("inv"))
        {
            commands.set(1, "inventory");
        }
        // Allow "the" to follow goto command. Remove it from commands if present
        if (commands.get(1).equals("goto"))
        {
            if (commands.get(2).equalsIgnoreCase("the"))
            {
                commands.remove(2);
            }
        }
    }

    // Check that the player input is populated
    public void checkValidInput() throws STAGException
    {
        // First check commands is not null; first entry is name so check for < 2
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
