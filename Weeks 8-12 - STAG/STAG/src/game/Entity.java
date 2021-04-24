package game;

public abstract class Entity
{
    private String name;
    private String description;
    private String entityType;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEntityType()
    {
        return entityType;
    }

    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }
}
