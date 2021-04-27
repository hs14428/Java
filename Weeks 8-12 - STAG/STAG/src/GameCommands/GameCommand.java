package GameCommands;

import GameExceptions.STAGException;
import Game.GameEngine;

public abstract class GameCommand
{
    private String commandType;

    public GameCommand()
    {
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

}
