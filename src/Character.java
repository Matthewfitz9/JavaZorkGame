//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private Room currentRoom;
    private List<Item> inventory = new ArrayList();

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

    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    public void listItems() {
        if (this.inventory.isEmpty()) {
            System.out.println("Your inventory is currently empty.");
        } else {
            for(Item item : this.inventory) {
                System.out.print(item.getName());
            }
        }

    }

    public void move(String direction) {
        Room nextRoom = this.currentRoom.getExit(direction);
        if (nextRoom != null) {
            this.currentRoom = nextRoom;
            System.out.println("You moved to: " + this.currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }

    }
}
