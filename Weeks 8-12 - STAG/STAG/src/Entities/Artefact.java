package Entities;

public class Artefact extends Entity
{
    public Artefact(String name, String description)
    {
        setName(name);
        setDescription(description);
        setEntityType("artefacts");
    }
}
