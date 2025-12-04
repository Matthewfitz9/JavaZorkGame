//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private Map<String, String> validCommands = new HashMap();

    public CommandWords() {
        this.validCommands.put("go", "Move to another room");
        this.validCommands.put("quit", "End the game");
        this.validCommands.put("help", "Show help");
        this.validCommands.put("look", "Look around");
        this.validCommands.put("eat", "Eat something");
        this.validCommands.put("leave", "Exit the room you are in");
        this.validCommands.put("inventory", "List current inventory");
        this.validCommands.put("take", "Pick up an item");
        this.validCommands.put("drop", "Drop the item");
        this.validCommands.put("open", "Opening");
        this.validCommands.put("buy", "Buy something");
        this.validCommands.put("drink", "Drink something");
        this.validCommands.put("get sick", "Getting sick");
        this.validCommands.put("read", "Reading a letter");
        this.validCommands.put("restart", "Restarting the game");
        this.validCommands.put("save", "Saving your game");
        this.validCommands.put("map", "Showing map");
        this.validCommands.put("use", "Using an item");

    }

    public boolean isCommand(String commandWord) {
        return this.validCommands.containsKey(commandWord);
    }

    public void showAll() {
        System.out.print("Valid commands are: ");

        for(String command : this.validCommands.keySet()) {
            System.out.print(command + " ");
        }
    }
}
