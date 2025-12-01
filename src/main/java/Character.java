import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Character {


    private String name;
    private Room currentRoom;
    private List<Item> inventory = new ArrayList<>();


    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
    }


    public String getName() {
        return this.name;
    }


    public Room getCurrentRoom() {
        return this.currentRoom;
    }


    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public Item removeItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }


    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
    public String getInventoryString() {
        if (inventory.isEmpty()) return "Inventory is empty.";
        StringBuilder sb = new StringBuilder("Inventory:\n");
        for (Item i : inventory) {
            sb.append("- ").append(i.getName()).append("\n");
        }
        return sb.toString();
    }


    public void listItems() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is currently empty.");
        } else {
            int i = 1;
            for (Item item : inventory) {
                System.out.println(i + ". " + item.getName());
                i++;
            }
        }
    }

    public List<Item> getInventory() {
        return inventory;
    }
}
