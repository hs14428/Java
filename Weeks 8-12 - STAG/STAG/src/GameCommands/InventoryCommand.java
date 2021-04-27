package GameCommands;

import Game.GameEngine;
import Entities.Player;

import java.util.ArrayList;

public class InventoryCommand extends GameCommand
{

    public InventoryCommand()
    {
        setCommandType("inventory");
    }

    @Override
    public String runCommand(GameEngine gameEngine)
    {
        Player currentPlayer = gameEngine.getCurrentPlayer();
        ArrayList<String> inventoryArrayList = new ArrayList<String>(currentPlayer.getInventory().keySet());
        String inventoryString = "";

        for (String s : inventoryArrayList)
        {
            inventoryString += s;
            inventoryString += "\n";
        }
        // If inventory is empty, tell the player so
        if (inventoryString.equals(""))
        {
            inventoryString = "Your inventory is empty";
        }
        return inventoryString;
    }
}
