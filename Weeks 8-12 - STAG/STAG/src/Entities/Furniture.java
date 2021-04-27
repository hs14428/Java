package Entities;

public class Furniture extends Entity
{
    private boolean furnitureUsed;

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

    public boolean getFurnitureUsed() { return furnitureUsed; }
}
