package GameCommands;

import GameExceptions.STAGException;
import Entities.Artefact;
import Entities.Entity;
import Game.GameEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GetCommand extends GameCommand
{
    public GetCommand()
    {
        setCommandType("get");
    }

    @Override
    public String runCommand(GameEngine gameEngine) throws STAGException
    {
        LinkedHashMap<String, Entity> locationArtefacts = gameEngine.getCurrentLocation().getArtefacts();
        ArrayList<String> artefactNames = new ArrayList<String>(locationArtefacts.keySet());
        String currentCommand = gameEngine.getNextCommand();
        Artefact artefactToPickup;
        String returnMessage;

        for (String s : artefactNames)
        {
            if (currentCommand.equals(s))
            {
                artefactToPickup = (Artefact) locationArtefacts.get(currentCommand.toLowerCase());
                // Add artefact to players inventory
                gameEngine.getCurrentPlayer().addToInv(artefactToPickup);
                // Then remove it from the current location
                gameEngine.getCurrentLocation().removeEntity(artefactToPickup.getEntityType(), artefactToPickup);
                // Then update the game map to reflect changes to location
                returnMessage = "You picked up a ";
                returnMessage += currentCommand;
                return  returnMessage;
            }
        }
        throw new STAGException("No item matches that description here. Check spelling and case sensitivity");
    }
}
