package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;

import java.util.LinkedHashMap;

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
