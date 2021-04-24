package game;

import java.util.LinkedHashMap;
import java.util.Set;

public class Player extends Entity
{
    private LinkedHashMap<String, Artefact> inventory = new LinkedHashMap<>();

    public Player(String playerName)
    {
        setName(playerName);
        setDescription("A person on a noble quest.");
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
