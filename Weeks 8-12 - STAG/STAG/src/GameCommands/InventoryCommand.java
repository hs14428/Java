package GameCommands;

import GameExceptions.STAGException;
import game.GameEngine;
import game.Player;

import java.util.Set;

public class InventoryCommand extends GameCommand
{

    public InventoryCommand()
    {
        setCommandType("inventory");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        Player currentPlayer = gameEngine.getCurrentPlayer();
        Set<String> inventorySet = currentPlayer.getInventory().keySet();
        String inventoryString = "";

        for (String s : inventorySet)
        {
            inventoryString += s;
            inventoryString += "\n";
        }
        return inventoryString;
    }
}
