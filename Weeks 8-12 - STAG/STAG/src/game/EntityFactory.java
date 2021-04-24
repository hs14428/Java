package game;

public class EntityFactory
{
    public Entity makeEntity(String entityType, String name, String description)
    {
        switch (entityType) {
            case "artefacts":
                return new Artefact(name, description);
            case "furniture":
                return new Furniture(name, description);
            case "characters":
                return new Character(name, description);
            default:
                return null;
        }
    }
}
