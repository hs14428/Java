package Entities;

public class Furniture extends Entity
{
    public Furniture(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("furniture");
    }
}
