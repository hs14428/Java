package game;

import GameExceptions.STAGException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class Action
{
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;
    private String entityType;
    private Entity entity;
    private int entityCount;

    public Action()
    {
    }

    public String performAction(GameEngine gameEngine) throws STAGException
    {
        // Check that word after action is valid
        checkValidAction(gameEngine);
        if (subjectCheck(gameEngine))
        {
            consumeEntities(gameEngine);
            produceEntities(gameEngine);
        }
        return narration;
    }

    public void checkValidAction(GameEngine gameEngine) throws STAGException
    {
        String currentCommand = gameEngine.getNextCommand();
        // Improve later - probs better to check name again location entities
        for (String s : subjects)
        {
            if (currentCommand.equals(s))
            {
                return;
            }
        }
        throw new STAGException("Invalid entity to perform action on");
    }

    public void produceEntities(GameEngine gameEngine) throws STAGException
    {
        String originalLocation = gameEngine.getCurrentLocation().getName();
        Location unplaced = gameEngine.getGameMap().get("unplaced");
        for (String s : produced)
        {
            // First check if a location or not
            if (!checkIfLocation(gameEngine, s))
            {
                if (s.equalsIgnoreCase("health"))
                {
                    gameEngine.getCurrentPlayer().increaseHealth();
                } else {
                    gameEngine.setCurrentLocation(unplaced);
                    System.out.println("gameLocation: "+gameEngine.getCurrentLocation().getName());
                    entityType = gameEngine.getCurrentLocation().getEntityType(s);
                    System.out.println(gameEngine.getCurrentLocation().getEntityDescriptions());
                    entity = gameEngine.getCurrentLocation().getEntity(entityType, s);
                    // Remove entity from unplaced
                    System.out.println("entity type: "+entityType);
                    System.out.println("entity: "+entity.getName());
                    gameEngine.getCurrentLocation().removeEntity(entityType, entity);
                    // Reset location back to original before making changes
                    gameEngine.setCurrentLocation(gameEngine.getGameMap().get(originalLocation));
                    // Add the produced entity to the original locations entities
                    gameEngine.getCurrentLocation().addEntity(entityType, entity);
                }
            } else {
                // Add the new location to available paths from original location
                gameEngine.getCurrentLocation().addPath(s);
            }
        }
    }

    public boolean checkIfLocation(GameEngine gameEngine, String entityName)
    {
//        Set<String> locationNames = gameEngine.getGameMap().keySet();
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

    public void consumeEntities(GameEngine gameEngine) throws STAGException
    {
        consumeLocationEntities(gameEngine);
        consumeInventoryEntities(gameEngine);
        consumeHealth(gameEngine);
    }

    public void consumeHealth(GameEngine gameEngine)
    {
        for (String s : consumed)
        {
            if (s.equalsIgnoreCase("health"))
            {
                if (gameEngine.getCurrentPlayer().getHealth() == 1)
                {
                    narration = "Oh dear, you died! You dropped all your items and respawned at the start";
                    dropItems(gameEngine);
                    gameEngine.getCurrentPlayer().resetHealth();
                    gameEngine.setCurrentLocation(gameEngine.getStartLocation());
                } else {
                    gameEngine.getCurrentPlayer().reduceHealth();
                }
            }
        }
    }

    public void dropItems(GameEngine gameEngine)
    {
        Set<String> playerInventory = gameEngine.getCurrentPlayer().getInventory().keySet();

        for (String s : playerInventory)
        {
            entity = gameEngine.getCurrentPlayer().getInventory().get(s);
            entityType = "artefacts";
            gameEngine.getCurrentPlayer().removeFromInv((Artefact)  entity);
            gameEngine.getCurrentLocation().addEntity(entityType, entity);
            gameEngine.getGameMap().put(gameEngine.getCurrentLocation().getName(), gameEngine.getCurrentLocation());
        }

    }

    public void consumeLocationEntities(GameEngine gameEngine) throws STAGException
    {
        ArrayList<String> locationEntityNames = gameEngine.getCurrentLocation().getEntityNames();
        for (String s : consumed)
        {
            for (String l : locationEntityNames)
            {
                if (s.equals(l))
                {
                    entityType = gameEngine.getCurrentLocation().getEntityType(s);
                    entity = gameEngine.getCurrentLocation().getEntity(entityType, s);
                    System.out.println("removing Location entity: "+entityType +", "+entity.getName());
                    gameEngine.getCurrentLocation().removeEntity(entityType, entity);
                }
            }
        }
    }

    public void consumeInventoryEntities(GameEngine gameEngine)
    {
//        Set<String> inventoryEntityNames = gameEngine.getCurrentPlayer().getInventory().keySet();
        ArrayList<String> inventoryEntityNames = new ArrayList<String>(gameEngine.getCurrentPlayer().getInventory().keySet());
        for (String s : consumed)
        {
            for (String i : inventoryEntityNames)
            {
                if (s.equals(i))
                {
                    entity = gameEngine.getCurrentPlayer().getInventory().get(s);
                    System.out.println("removing inv entity: "+entity.getName());
                    gameEngine.getCurrentPlayer().removeFromInv((Artefact) entity);
                }
            }
        }
    }

    public void checkLocationEntities(GameEngine gameEngine)
    {
        ArrayList<String> locationEntityNames = gameEngine.getCurrentLocation().getEntityNames();
        for (String s : subjects)
        {
            System.out.println("check Loc subject name: "+ s);
            for (String l : locationEntityNames)
            {
                System.out.println("check loc entity name: " + l);
                if (s.equals(l))
                {
                    // If one of subject entities is present in the location, add to count
                    entityCount++;
                    // Also set the furn
                }
            }
        }
    }

    public void checkInventory(GameEngine gameEngine)
    {
//        Set<String> inventoryEntityNames = gameEngine.getCurrentPlayer().getInventory().keySet();
        ArrayList<String> inventoryEntityNames = new ArrayList<String>(gameEngine.getCurrentPlayer().getInventory().keySet());
        for (String s : subjects)
        {
            System.out.println("check inv subject name: "+ s);
            for (String i : inventoryEntityNames)
            {
                System.out.println("check inv entity name: " + i);
                if (s.equals(i))
                {
                    // If one of subject entities is present in players inventory, add to count
                    entityCount++;
                }
            }
        }
    }

    // Add feature to say e.g. door is already open
    public boolean subjectCheck(GameEngine gameEngine) throws STAGException
    {
        entityCount = 0;
        checkLocationEntities(gameEngine);
        System.out.println("entityCount post location:" + entityCount);
        checkInventory(gameEngine);
        System.out.println("entityCount post inventory:" + entityCount);
        if (entityCount == subjects.size())
        {
            return true;
        }
        throw new STAGException("Not enough subjects present in environment to perform this action");
    }

    public void setNarration(String narration)
    {
        this.narration = narration;
    }

    public String getNarration()
    {
        return narration;
    }

    public void setSubjects(ArrayList<String> subjects)
    {
        this.subjects = subjects;
    }

    public String printSubjects()
    {
        String subjectsString = "";

        for (String s : subjects)
        {
            subjectsString += s;
        }
        return subjectsString;
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
