package GameCommands;

import GameExceptions.STAGException;
import game.Artefact;
import game.GameEngine;

import java.util.LinkedHashMap;
import java.util.Set;

public class DropCommand extends GameCommand
{
    public DropCommand()
    {
        setCommandType("drop");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        LinkedHashMap<String, Artefact> inventory = gameEngine.getCurrentPlayer().getInventory();
        Set<String> inventoryItems = inventory.keySet();
        String currentCommand = gameEngine.getNextCommand();
        Artefact artefactToDrop;
        String returnMessage;

        for (String s : inventoryItems)
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                artefactToDrop = gameEngine.getCurrentPlayer().getInventory().get(currentCommand);
                // Remove artefact from players inventory
                gameEngine.getCurrentPlayer().removeFromInv(artefactToDrop);
                // Then re-add it to the current location
//                gameEngine.getCurrentLocation().addArtefact(artefactToDrop);
                gameEngine.getCurrentLocation().addEntity(artefactToDrop.getEntityType(), artefactToDrop);
                // Then update the game map to reflect changes to location
                // Not sure if the below is needed as location state seems to change automatically, without updating gamemap
                gameEngine.getGameMap().put(gameEngine.getCurrentLocation().getName(), gameEngine.getCurrentLocation());
                returnMessage = "You dropped a ";
                returnMessage += artefactToDrop.getName();
                return returnMessage;
            }
        }
        throw new STAGException("You do not have that item in your inventory");
    }
}
