package GameCommands;

import GameExceptions.STAGException;
import Game.GameEngine;
import Entities.Location;

import java.util.ArrayList;

public class GotoCommand extends GameCommand
{

    public GotoCommand()
    {
        setCommandType("goto");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        String currentCommand = gameEngine.getNextCommand().toLowerCase();

        for (String s : gameEngine.getCurrentLocation().getPaths())
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                // If valid location to move to, go there and look around automatically
                movePlayer(gameEngine, currentCommand);
                return new LookCommand().runCommand(gameEngine);
            }
        }
        return checkDescriptionMatch(gameEngine);
    }

    public void movePlayer(GameEngine gameEngine, String location)
    {
        Location  nextLocation = gameEngine.getGameMap().get(location);
        // Remove current player for location they are leaving
        gameEngine.getCurrentLocation().removePlayer(gameEngine.getCurrentPlayer().getName());
        gameEngine.setCurrentLocation(nextLocation);
        // Set currentPlayer location = set location
        gameEngine.getCurrentPlayer().setPlayerLocation(nextLocation.getName());
        // Add the current player to the new location they have just arrived at
        gameEngine.getCurrentLocation().addPlayer(gameEngine.getCurrentPlayer().getName());
    }

    // Checks if the user says goto a deep dark forest, as opposed to goto forest
    public String checkDescriptionMatch(GameEngine gameEngine) throws STAGException
    {
        ArrayList<String> commands = gameEngine.getCommands();
        // Remove name and first command of commands ArrayList
        commands.remove(0);
        commands.remove(0);
        String stringifiedCommands = stringify(commands);
        String locationDescription;
        String locationName;
        String errorMessage;

        for (String s : gameEngine.getCurrentLocation().getPaths())
        {
            locationName = gameEngine.getGameMap().get(s).getName();
            locationDescription = gameEngine.getGameMap().get(s).getDescription();
            // Check if inputted commands is equal to the a possible path description
            if (stringifiedCommands.equalsIgnoreCase(locationDescription))
            {
                // If valid location to move to, go there and look around automatically
                movePlayer(gameEngine, locationName);
                return new LookCommand().runCommand(gameEngine);
            }
        }
        errorMessage = "Invalid path selected. Valid Paths: ";
        errorMessage += gameEngine.getCurrentLocation().getPathsString();
        throw new STAGException(errorMessage);
    }

    public String stringify(ArrayList<String> arrayList)
    {
        String stringifiedArrList = "";
        for (String s : arrayList)
        {
            stringifiedArrList += s;
            stringifiedArrList += " ";
        }
        stringifiedArrList = stringifiedArrList.trim();
        return stringifiedArrList;
    }
}
