package GameCommands;

import Game.GameEngine;

public class HealthCommand extends GameCommand
{

    public HealthCommand()
    {
        setCommandType("health");
    }

    @Override
    public String runCommand(GameEngine gameEngine)
    {
        String returnMessage;
        int currentPlayerHealth = gameEngine.getCurrentPlayer().getHealth();
        returnMessage = "Your health is: ";
        returnMessage += String.valueOf(currentPlayerHealth);
        return returnMessage;
    }
}
