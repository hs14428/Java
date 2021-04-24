package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;

import java.util.LinkedHashMap;

public abstract class GameCommand
{
    private String commandType;
//    private String currentCommand;
//    private GameEngine gameEngine;
//    private LinkedHashMap<String, Location> gameMap;
//    private Location currentLocation;
//    private String gameState;

    public GameCommand()
    {
//        this.gameEngine = gameEngine;
//        currentLocation = this.gameEngine.getCurrentLocation();
//        currentCommand = this.gameEngine.getCurrentCommand();
//        gameMap = this.gameEngine.getGameMap();
    }

    public abstract String runCommand(GameEngine gameEngine) throws STAGException;

    public String getCommandType()
    {
        return commandType;
    }

    public void setCommandType(String commandType)
    {
        this.commandType = commandType;
    }
//
//    public GameEngine getGameEngine()
//    {
//        return gameEngine;
//    }
//
//    public LinkedHashMap<String, Location> getGameMap()
//    {
//        return gameMap;
//    }

//    public Location getCurrentLocation()
//    {
//        return currentLocation;
//    }
//
//    public void setCurrentLocation(Location newLocation)
//    {
//        currentLocation = newLocation;
//    }
//
//    public String getCurrentCommand()
//    {
//        return gameEngine.getCurrentCommand();
//    }
//
//    public void setCurrentCommand(String command) { currentCommand = command; }
//
//    public String getGameState()
//    {
//        return gameState;
//    }
}
