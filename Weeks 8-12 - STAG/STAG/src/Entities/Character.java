package Entities;

public class Character extends Entity
{
    public Character(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("characters");
    }
}
