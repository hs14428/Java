package game;

import java.util.LinkedHashMap;
import java.util.Set;

public class Player extends Entity
{
    private LinkedHashMap<String, Artefact> inventory = new LinkedHashMap<>();
    private int health;

    public Player(String playerName)
    {
        setName(playerName);
        setDescription("A hero on a noble quest.");
        health = 3;
    }

    public int getHealth()
    {
        return health;
    }

    public void resetHealth()
    {
        health = 3;
    }

    public void reduceHealth()
    {
        health--;
    }

    public void increaseHealth()
    {
        health++;
    }

    public LinkedHashMap<String, Artefact> getInventory()
    {
        return inventory;
    }

    public void addToInv(Artefact artefact)
    {
        inventory.put(artefact.getName(), artefact);
    }

    public void removeFromInv(Artefact artefact)
    {
        inventory.remove(artefact.getName());
    }
}
