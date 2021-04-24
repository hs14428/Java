package game;

public class Furniture extends Entity
{
    boolean furnitureUsed;

    public Furniture(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("furniture");
        furnitureUsed = false;
    }

    public void setFurnitureUsed()
    {
        furnitureUsed = true;
    }
}
