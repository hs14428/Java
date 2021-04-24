package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;
import game.Location;

public class GotoCommand extends GameCommand
{
    public GotoCommand()
    {
        setCommandType("goto");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        String errorMessage;
        Location nextLocation;
        String currentCommand = gameEngine.getNextCommand();

        for (String s : gameEngine.getCurrentLocation().getPaths())
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                nextLocation = gameEngine.getGameMap().get(currentCommand);
                gameEngine.setCurrentLocation(nextLocation);
                return new LookCommand().runCommand(gameEngine);
            }
        }
        errorMessage = "Invalid path selected. Valid Paths: ";
        errorMessage += gameEngine.getCurrentLocation().getPathsString();
        throw new STAGException(errorMessage);
    }
}
