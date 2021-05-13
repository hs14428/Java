package Game;

import Entities.Artefact;
import Entities.Entity;
import Entities.Furniture;
import Entities.Location;
import GameExceptions.STAGException;

import java.util.ArrayList;

public class Action
{
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;
    private String entityType;
    private Entity entity;
    private String actionName;
    private int entityCount;
    private boolean isDead;

    public Action()
    {
        isDead = false;
    }

    public void setActionName(String actionName)
    {
        this.actionName = actionName;
    }

    public String getActionName()
    {
        return actionName;
    }

    public boolean performAction(GameEngine gameEngine) throws STAGException
    {
        // Check that word after action is valid
        if (!checkValidAction(gameEngine))
        {
            return false;
        }
        if (subjectCheck(gameEngine))
        {
            consumeEntities(gameEngine);
            produceEntities(gameEngine);
        }
        return true;
    }

    // Check that the word inputted after the action is a valid subject type by comparing to subjects arraylist
    public boolean checkValidAction(GameEngine gameEngine) throws STAGException
    {
        String currentCommand = gameEngine.getNextCommand().toLowerCase();
        for (String s : subjects)
        {
            if (currentCommand.equals(s))
            {
                return true;
            }
        }
        return false;
    }

    // Method to produce any entities that need to be made after successful action call
    public void produceEntities(GameEngine gameEngine) throws STAGException
    {
        String originalLocation = gameEngine.getCurrentLocation().getName();
        Location unplaced = gameEngine.getGameMap().get("unplaced");
        for (String s : produced)
        {
            // First check if a location or not
            if (!checkIfLocation(gameEngine, s))
            {
                // Then check if producing health - this is a special case
                if (s.equalsIgnoreCase("health"))
                {
                    gameEngine.getCurrentPlayer().increaseHealth();
                } else if (consumed.isEmpty()) {
                    produceRepeatEntity(gameEngine);
                }
                else {
                    gameEngine.setCurrentLocation(unplaced);
                    entityType = gameEngine.getCurrentLocation().getEntityType(s);
                    entity = gameEngine.getCurrentLocation().getEntity(entityType, s);
                    // Remove entity from unplaced
                    gameEngine.getCurrentLocation().removeEntity(entityType, entity);
                    // Reset location back to original before making changes
                    gameEngine.setCurrentLocation(gameEngine.getGameMap().get(originalLocation));
                    // Add the produced entity to the original locations entities
                    gameEngine.getCurrentLocation().addEntity(entityType, entity);
                }
            } else {
                // If it is a location then:
                // Add the new location to available paths from original location
                gameEngine.getCurrentLocation().addPath(s);
            }
        }
    }

    // Method for special cases where action can be performed multiple times due to no consumption of entities
    public void produceRepeatEntity(GameEngine gameEngine) throws STAGException
    {
        // Can't have multiple entities of same name, so need to search for produced entity to move it
        for (String s : produced)
        {
            // Search each location for produced entity
            for (Location l : gameEngine.getGameMap().values())
            {
                ArrayList<String> entityNames = l.getEntityNames();
                // Check all entities in each location
                for (String e : entityNames)
                {
                    // If find a match, remove that entity from that location
                    if (e.equals(s))
                    {
                        entityType = gameEngine.getGameMap().get(l.getName()).getEntityType(s);
                        entity = gameEngine.getGameMap().get(l.getName()).getEntity(entityType, s);
                        l.removeEntity(entityType, entity);
                        // And add it to the current location
                        gameEngine.getCurrentLocation().addEntity(entityType, entity);
                    }
                }
            }
        }
    }

    // Method to check if the produced output of an action is a location
    public boolean checkIfLocation(GameEngine gameEngine, String entityName)
    {
        ArrayList<String> locationNames = new ArrayList<String>(gameEngine.getGameMap().keySet());
        for (String s : locationNames)
        {
            if (entityName.equals(s))
            {
                return true;
            }
        }
        return false;
    }

    // Master method to consume all relevant entities after a succesful action
    public void consumeEntities(GameEngine gameEngine) throws STAGException
    {
        consumeLocationEntities(gameEngine);
        consumeInventoryEntities(gameEngine);
        consumeHealth(gameEngine);
    }

    // Consumes health if its included in the consumed array
    public void consumeHealth(GameEngine gameEngine)
    {
        for (String s : consumed)
        {
            if (s.equalsIgnoreCase("health"))
            {
                // If player health is at 1, then they are about to die and special things must happen
                if (gameEngine.getCurrentPlayer().getHealth() == 1)
                {
                    // Set isDead to true so that the user can be told that they have died
                    isDead = true;
                    // Drop all the current inventory items in the current location.
                    dropItems(gameEngine);
                    gameEngine.getCurrentPlayer().resetHealth();
                    // Remove player from location as they have died
                    gameEngine.getCurrentLocation().removePlayer(gameEngine.getCurrentPlayer().getName());
                    // Add player back to start location and reset their location
                    gameEngine.getStartLocation().addPlayer(gameEngine.getCurrentPlayer().getName());
                    gameEngine.getCurrentPlayer().setPlayerLocation(gameEngine.getStartLocation().getName());
                } else {
                    isDead = false;
                    gameEngine.getCurrentPlayer().reduceHealth();
                }
            }
        }
    }

    // Method for dropping items, should a player's health drop to 0
    public void dropItems(GameEngine gameEngine)
    {
        ArrayList<String> playerInventory = new ArrayList<String>(gameEngine.getCurrentPlayer().getInventory().keySet());

        for (String s : playerInventory)
        {
            entity = gameEngine.getCurrentPlayer().getInventory().get(s);
            entityType = "artefacts";
            gameEngine.getCurrentPlayer().removeFromInv((Artefact)  entity);
            gameEngine.getCurrentLocation().addEntity(entityType, entity);
            gameEngine.getGameMap().put(gameEngine.getCurrentLocation().getName(), gameEngine.getCurrentLocation());
        }

    }

    // Consume entities from location if an action consumed entity
    public void consumeLocationEntities(GameEngine gameEngine) throws STAGException
    {
        ArrayList<String> locationEntityNames = gameEngine.getCurrentLocation().getEntityNames();
        for (String s : consumed)
        {
            for (String l : locationEntityNames)
            {
                if (s.equals(l))
                {
                    // If location item matches a consumed entity, remove from location
                    entityType = gameEngine.getCurrentLocation().getEntityType(s);
                    entity = gameEngine.getCurrentLocation().getEntity(entityType, s);
                    gameEngine.getCurrentLocation().removeEntity(entityType, entity);
                }
            }
        }
    }

    // Consume entities from inventory if an action consumed entity
    public void consumeInventoryEntities(GameEngine gameEngine)
    {
        ArrayList<String> inventoryEntityNames = new ArrayList<String>(gameEngine.getCurrentPlayer().getInventory().keySet());
        for (String s : consumed)
        {
            for (String i : inventoryEntityNames)
            {
                if (s.equals(i))
                {
                    // If inventory item matches a consumed entity, remove from inventory
                    entity = gameEngine.getCurrentPlayer().getInventory().get(s);
                    gameEngine.getCurrentPlayer().removeFromInv((Artefact) entity);
                }
            }
        }
    }

    // Check location for action subject entities by counting total entities
    public void checkLocationEntities(GameEngine gameEngine)
    {
        ArrayList<String> locationEntityNames = gameEngine.getCurrentLocation().getEntityNames();
        for (String s : subjects)
        {
            for (String l : locationEntityNames)
            {
                if (s.equals(l))
                {
                    // If one of subject entities is present in the location, add to count
                    entityCount++;
                }
            }
        }
    }

    // Check inventory for action subject entities
    public void checkInventoryEntities(GameEngine gameEngine)
    {
        ArrayList<String> inventoryEntityNames = new ArrayList<String>(gameEngine.getCurrentPlayer().getInventory().keySet());
        for (String s : subjects)
        {
            for (String i : inventoryEntityNames)
            {
                if (s.equals(i))
                {
                    // If one of subject entities is present in players inventory, add to count
                    entityCount++;
                }
            }
        }
    }

    // Check if all the required subjects are present in the inventory and current location to perform and action
    public boolean subjectCheck(GameEngine gameEngine) throws STAGException
    {
        entityCount = 0;
        checkLocationEntities(gameEngine);
        checkInventoryEntities(gameEngine);
        // If the checkers have counted the correct number of subjects required, return true
        if (entityCount == subjects.size())
        {
            return true;
        }
        String errorMessage = "You don't have the tools/items/surroundings required to do this action";
        throw new STAGException(errorMessage);
    }

    public void setNarration(String narration)
    {
        this.narration = narration;
    }

    // Special getter that returns normal action narration, unless you deeeeed.
    public String getNarration()
    {
        if (!isDead)
        {
            return narration;
        } else {
            return "Oh dear, you died! You dropped all your items and respawned at the start";
        }
    }

    public void setSubjects(ArrayList<String> subjects)
    {
        this.subjects = subjects;
    }

    public void setConsumed(ArrayList<String> subjects)
    {
        this.consumed = subjects;
    }

    public void setProduced(ArrayList<String> subjects)
    {
        this.produced = subjects;
    }
}
