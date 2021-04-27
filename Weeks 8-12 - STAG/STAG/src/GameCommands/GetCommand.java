package GameCommands;

import GameExceptions.STAGException;
import game.Artefact;
import game.Entity;
import game.GameEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

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
//        Set<String> artefactNames = locationArtefacts.keySet();
        ArrayList<String> artefactNames = new ArrayList<String>(locationArtefacts.keySet());
        String currentCommand = gameEngine.getNextCommand();
        Artefact artefactToPickup;
        String returnMessage;

        for (String s : artefactNames)
        {
            if (currentCommand.equalsIgnoreCase(s))
            {
                artefactToPickup = (Artefact) locationArtefacts.get(currentCommand);
                // Add artefact to players inventory
                gameEngine.getCurrentPlayer().addToInv(artefactToPickup);
                // Then remove it from the current location
//                gameEngine.getCurrentLocation().removeArtefact(artefactToPickup);
                gameEngine.getCurrentLocation().removeEntity(artefactToPickup.getEntityType(), artefactToPickup);
                // Then update the game map to reflect changes to location
                // Not sure if the below is needed as location state seems to change automatically, without updating gamemap
                gameEngine.getGameMap().put(gameEngine.getCurrentLocation().getName(), gameEngine.getCurrentLocation());
                returnMessage = "You picked up a ";
                returnMessage += currentCommand;
                return  returnMessage;
            }
        }
        throw new STAGException("No item matches that description here");
    }
}
