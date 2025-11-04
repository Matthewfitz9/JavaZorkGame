//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class ZorkULGame {
    private Parser parser;
    private Character player;

    public ZorkULGame() {
        this.createRooms();
        this.parser = new Parser();
    }

    private void createRooms() {
        Room outside = new Room("outside the main entrance of the university");
        Room theatre = new Room("in a lecture theatre");
        Room pub = new Room("in the campus pub");
        Room lab = new Room("in a computing lab");
        Room office = new Room("in the computing admin office");
        Room toilet = new Room("in the toilet");
        Item flower = new Item("Flower", "A beautiful red flower.");
        Item fountain = new Item("Fountain", "A marble fountain with clear water.");
        Item shovel = new Item("Shovel", "A sturdy shovel, slightly muddy.");
        toilet.addItem(flower);
        toilet.addItem(fountain);
        toilet.addItem(shovel);
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        theatre.setExit("west", outside);
        pub.setExit("east", outside);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);
        toilet.setExit("north", pub);
        toilet.setExit("east", lab);
        this.player = new Character("player", outside);
    }

    public void play() {
        this.printWelcome();

        Command command;
        for(boolean finished = false; !finished; finished = this.processCommand(command)) {
            command = this.parser.getCommand();
        }

        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the University adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(this.player.getCurrentRoom().getLongDescription());
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();
        if (commandWord == null) {
            System.out.println("I don't understand your command...");
            return false;
        } else {
            switch (commandWord) {
                case "help":
                    this.printHelp();
                    break;
                case "go":
                    this.goRoom(command);
                    break;
                case "quit":
                    if (command.hasSecondWord()) {
                        System.out.println("Quit what?");
                        return false;
                    }

                    return true;
                case "take":
                    this.takeItem(command);
                    break;
                default:
                    System.out.println("I don't know what you mean...");
            }

            return false;
        }
    }

    private void takeItem(Command command) {
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the university.");
        System.out.print("Your command words are: ");
        this.parser.showCommands();
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
        } else {
            String direction = command.getSecondWord();
            Room nextRoom = this.player.getCurrentRoom().getExit(direction);
            if (nextRoom == null) {
                System.out.println("There is no door!");
            } else {
                this.player.setCurrentRoom(nextRoom);
                System.out.println(this.player.getCurrentRoom().getLongDescription());
            }

        }
    }

    public static void main(String[] args) {
        ZorkULGame game = new ZorkULGame();
        game.play();
    }
}
