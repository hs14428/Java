package GameCommands;

import GameExceptions.STAGException;
import Game.GameEngine;

import java.util.ArrayList;

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
        gameState = checkForOtherPlayers(gameEngine, gameState);
        return gameState;
    }

    public String checkForOtherPlayers(GameEngine gameEngine, String gameState)
    {
        ArrayList<String> otherPlayers;
        if (gameEngine.getCurrentLocation().getOtherPlayers().size() > 1)
        {
            otherPlayers = new ArrayList<String>(gameEngine.getCurrentLocation().getOtherPlayers().keySet());
            gameState += "You can also see some other players:\n";
            for (String s : otherPlayers)
            {
                // If player in current location is NOT equal to current player, add to list
                if (!gameEngine.getCurrentPlayer().getName().equals(s))
                {
                    gameState += s;
                    gameState += "\n";
                }
            }
            return gameState;
        }
        return gameState;
    }
}
