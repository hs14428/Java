package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;
import game.Player;

public class HealthCommand extends GameCommand
{

    public HealthCommand()
    {
        setCommandType("health");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        String returnMessage;
        int currentPlayerHealth = gameEngine.getCurrentPlayer().getHealth();
        returnMessage = "Your health is: ";
        returnMessage += String.valueOf(currentPlayerHealth);
        return returnMessage;
    }
}
