package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;

public class LookCommand extends GameCommand
{

    public LookCommand()
    {
        setCommandType("look");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        String gameState = "";
        if (gameEngine.getCurrentLocation() == null)
        {
            throw new STAGException("Somehow you are not in any location!?");
        }
        gameState += gameEngine.getCurrentLocation().getDescription();
        gameState += ". You can see:\n";
        gameState += gameEngine.getCurrentLocation().getEntityDescriptions();
        gameState += "You can access from here:\n";
        gameState += gameEngine.getCurrentLocation().getPathsString();
//        System.out.println(gameState);
        return gameState;
    }
}
