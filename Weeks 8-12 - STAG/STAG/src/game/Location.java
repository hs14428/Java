package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

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
    }

    public LinkedHashMap<String, Entity> getArtefacts()
    {
        return artefacts;
    }

    public void addEntity(String entityType, String name, String description)
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

    public Set<String> getAllEntityNames(String entityType)
    {
        Set<String> entityNames;
        switch (entityType) {
            case "artefacts":
                entityNames = artefacts.keySet();
                return entityNames;
            case "furniture":
                entityNames = furniture.keySet();
                return entityNames;
            case "characters":
                entityNames = characters.keySet();
                return entityNames;
            default:
                return null;
        }
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
