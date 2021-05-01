package Entities;

import GameExceptions.STAGException;

public class EntityFactory
{
    public Entity makeEntity(String entityType, String name, String description) throws STAGException
    {
        switch (entityType) {
            case "artefacts":
                return new Artefact(name, description);
            case "furniture":
                return new Furniture(name, description);
            case "characters":
                return new Character(name, description);
            default:
                throw new STAGException("Failed to create Entity in the factory");
        }
    }
}
