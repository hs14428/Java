package game;

import GameExceptions.STAGException;

import java.util.*;

public class Location extends Entity
{
    private ArrayList<String> pathLocations = new ArrayList<>();
    private LinkedHashMap<String, Entity> artefacts = new LinkedHashMap<>();
    private LinkedHashMap<String, Entity> furniture = new LinkedHashMap<>();
    private LinkedHashMap<String, Entity> characters = new LinkedHashMap<>();

    public Location(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("location");
    }

    public LinkedHashMap<String, Entity> getArtefacts()
    {
        return artefacts;
    }

    public void addNewEntity(String entityType, String name, String description)
    {
        EntityFactory entityFactory = new EntityFactory();
        Entity newEntity = entityFactory.makeEntity(entityType, name, description);
        switch (entityType) {
            case "artefacts":
                System.out.println("Adding artefact");
                artefacts.put(name, newEntity);
                System.out.println(artefacts.get(name).getDescription());
                return;
            case "furniture":
                System.out.println("Adding furniture");
                furniture.put(name, newEntity);
                return;
            case "characters":
                System.out.println("Adding character");
                characters.put(name, newEntity);
                return;
            default:
        }
    }

    public Entity getEntity(String entityType, String name)
    {
        switch (entityType) {
            case "artefacts":
                System.out.println("Getting artefact");
                return artefacts.get(name);
            case "furniture":
                System.out.println("Getting furniture");
                return furniture.get(name);
            case "characters":
                System.out.println("Getting character");
                return characters.get(name);
            default:
                return null;
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

    // remove
    public void printPaths()
    {
        for (String pathLocation : pathLocations)
        {
            System.out.println(pathLocation);
        }
    }

    public ArrayList<String> getEntityNames()
    {
        Set<String> entityNames = new HashSet<String>();
        entityNames.addAll(artefacts.keySet());
        entityNames.addAll(furniture.keySet());
        entityNames.addAll(characters.keySet());
        return new ArrayList<String>(entityNames);
    }

    public String getEntityType(String entityName) throws STAGException
    {
        Set<Map.Entry<String, Entity>> pairs = artefacts.entrySet();
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
            case "artefact":
                artefacts.put(entity.getName(), entity);
                return;
            case "furniture":
                furniture.put(entity.getName(), entity);
                return;
            case "character":
                characters.put(entity.getName(), entity);
                return;
            default:
        }
    }

    public void removeEntity(String entityType, Entity entity)
    {
        switch (entityType) {
            case "artefact":
                artefacts.remove(entity.getName());
                return;
            case "furniture":
                furniture.remove(entity.getName());
                return;
            case "character":
                characters.remove(entity.getName());
                return;
            default:
        }
    }

    @Override
    public String toString()
    {
        return getDescription();
    }

    public void removeArtefact(Artefact artefact)
    {
        artefacts.remove(artefact.getName());
    }

    public void addArtefact(Artefact artefact)
    {
        artefacts.put(artefact.getName(), artefact);
    }

    public void addFurniture(String name, String description)
    {
        Furniture furniture = new Furniture(name, description);
        this.furniture.put(name, furniture);
    }

    public void addCharacter(String name, String description)
    {
        Character character = new Character(name, description);
        characters.put(name, character);
    }

}
