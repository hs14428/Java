package Entities;

import GameExceptions.STAGException;

import java.util.*;

public class Location extends Entity
{
    private final ArrayList<String> pathLocations = new ArrayList<>();
    private final LinkedHashMap<String, Entity> artefacts = new LinkedHashMap<>();
    private final LinkedHashMap<String, Entity> furniture = new LinkedHashMap<>();
    private final LinkedHashMap<String, Entity> characters = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> otherPlayers = new LinkedHashMap<>();

    public Location(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("location");
    }

    public void addPlayer(String playerName)
    {
        otherPlayers.put(playerName, playerName);
    }

    public void removePlayer(String playerName)
    {
        String playerToRemove = null;
        for (String s : otherPlayers.keySet())
        {
            if (playerName.equals(s))
            {
                playerToRemove = s;
            }
        }
        // Remove player after iterator has looped through keySet to avoid ConcurrentModificationException
        otherPlayers.remove(playerToRemove);
    }

    public LinkedHashMap<String, String> getOtherPlayers()
    {
        return otherPlayers;
    }

    public LinkedHashMap<String, Entity> getArtefacts()
    {
        return artefacts;
    }

    public void addNewEntity(String entityType, String name, String description) throws STAGException
    {
        EntityFactory entityFactory = new EntityFactory();
        Entity newEntity = entityFactory.makeEntity(entityType, name, description);
        switch (entityType) {
            case "artefacts":
                artefacts.put(name, newEntity);
                System.out.println(artefacts.get(name).getDescription());
                return;
            case "furniture":
                furniture.put(name, newEntity);
                return;
            case "characters":
                characters.put(name, newEntity);
                return;
            default:
        }
    }

    public Entity getEntity(String entityType, String name)
    {
        switch (entityType) {
            case "artefacts":
                return artefacts.get(name);
            case "furniture":
                return furniture.get(name);
            case "characters":
                return characters.get(name);
            default:
                return null;
        }
    }

    public String getEntityType(String entityName) throws STAGException
    {
        ArrayList<Map.Entry<String, Entity>> pairs = new ArrayList<Map.Entry<String, Entity>>(artefacts.entrySet());
        pairs.addAll(furniture.entrySet());
        pairs.addAll(characters.entrySet());

        for (Map.Entry<String, Entity> e : pairs)
        {
            if (e.getKey().equals(entityName))
            {
                return e.getValue().getEntityType();
            }
        }
        throw new STAGException("No entity of that name present in this location");
    }

    // Can probably remove and just use hashmap keySet
    public ArrayList<String> getEntityNames()
    {
        Set<String> entityNames = new HashSet<String>();
        entityNames.addAll(artefacts.keySet());
        entityNames.addAll(furniture.keySet());
        entityNames.addAll(characters.keySet());
        return new ArrayList<String>(entityNames);
    }

    public String getEntityDescriptions()
    {
        String descriptionsString = "";
        ArrayList<String> descriptionArrayList = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<Entity>(artefacts.values());
        entities.addAll(furniture.values());
        entities.addAll(characters.values());

        for (Entity entity : entities)
        {
            descriptionArrayList.add(entity.getDescription());
        }
        for (String s : descriptionArrayList)
        {
            descriptionsString += s;
            descriptionsString += "\n";
        }

        return descriptionsString;
    }

    public void addEntity(String entityType, Entity entity)
    {
        switch (entityType) {
            case "artefacts":
                artefacts.put(entity.getName(), entity);
                return;
            case "furniture":
                furniture.put(entity.getName(), entity);
                return;
            case "characters":
                characters.put(entity.getName(), entity);
                return;
            default:
        }
    }

    public void removeEntity(String entityType, Entity entity)
    {
        switch (entityType) {
            case "artefacts":
                artefacts.remove(entity.getName());
                return;
            case "furniture":
                furniture.remove(entity.getName());
                return;
            case "characters":
                characters.remove(entity.getName());
                return;
            default:
        }
    }

    public void addPath(String locationName)
    {
        pathLocations.add(locationName);
    }

    public ArrayList<String> getPaths()
    {
        return pathLocations;
    }

    public String getPathsString()
    {
        String pathList = "";
        for (String pathLocation : pathLocations)
        {
            pathList += pathLocation;
            pathList += "\n";
        }
        return pathList;
    }

    public LinkedHashMap<String, Entity> getFurniture()
    {
        return furniture;
    }

    @Override
    public String toString()
    {
        return getDescription();
    }
}
