//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.*;


public class ZorkULGame {
    int j = 0;
    static int s = 0;
    private Character player;
    private Room cityCentre;
    private Thread beerTimerThread = null;
    private boolean timerRunning = false;
    private int timeLeft = 60;
    private String timerMessage = "";
    private java.util.List<Room> allRooms = new java.util.ArrayList<>();
    private boolean gameOver = false;
    private boolean skip = false;


    public ZorkULGame() {
        createRooms();
        player = new Character("Main player", cityCentre);

        if (!skip) {   // <---- only load save if not restarting
            loadGame();
        } // Try to load

        // If no envelope was loaded (new game), give starting envelope
        if (!player.hasItem("Envelope (Open Me)")) {
            player.addItem(new Item("Envelope (Open Me)", "A closed envelope"));
        }
    }


    private void createRooms() {
        cityCentre = new Room("outside in the city centre");
        Room pub = new Room("in the local pub");
        Room skyscraper = new Room("inside the skyscraper");
        Room office = new Room("in the computing admin office");
        Room toilet = new Room("in the toilet");
        QuizRoom quiz = new QuizRoom("in quiz room");


        allRooms.add(cityCentre);
        allRooms.add(pub);
        allRooms.add(skyscraper);
        allRooms.add(office);
        allRooms.add(toilet);
        allRooms.add(quiz);

        Item flower = new Item("Flower", "A beautiful red flower.");
        Item fountain = new Item("Fountain", "A marble fountain with clear water.");
        Item chest = new Item("Chest", "Chest in the corner");

        toilet.addItem(flower);
        toilet.addItem(fountain);
        toilet.addItem(new ShovelItem());

        pub.addItem(chest);

        cityCentre.setExit("north", quiz);
        cityCentre.setExit("east", skyscraper);
        cityCentre.setExit("west", pub);
        quiz.setExit("south", cityCentre);
        pub.setExit("east", cityCentre);
        skyscraper.setExit("west", cityCentre);
        skyscraper.setExit("south", office);
        office.setExit("north", skyscraper);
        office.setExit("west", toilet);
        toilet.setExit("north", pub);
        toilet.setExit("east", office);
        pub.setExit("south", toilet);


    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public static Direction fromString(String input) {
            switch (input.toLowerCase()) {
                case "north":
                    return Direction.NORTH;
                case "east":
                    return Direction.EAST;
                case "south":
                    return Direction.SOUTH;
                case "west":
                    return Direction.WEST;
                default:
                    return null;
            }
        }
    }


    public Character getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }


    public String printWelcome() {
        return "Welcome to the Tokyo Drift :)\nType 'help' if you need help.\nYou look down to your feet and see a closed envelope with your name on it.\n\n" + player.getCurrentRoom().getLongDescription() + "\n";

    }


    private String printHelp() {
        return "Commands:\ngo, open, take, drop, inventory,\nbuy, drink, read, use, save,\nrestart, map, help";
    }


    private String readLetter(Command command) {

        String second = command.getSecondWord();

        if (second == null) {
            return "Read what?";
        }

        if (!second.equalsIgnoreCase("letter")) {
            return "You cannot read that.";
        }

        if (!player.hasItem("Envelope (Open Me)")) {
            return "You do not have the envelope anymore.";
        }

        if (j == 0) {
            return "You must open the envelope before reading it.";
        }

        return "In this game you must navigate around the map completing challenges and collecting items with the goal of getting into the skyscraper.\nGood Luck.";
    }


    private String openEnvelope() {
        j++;
        return "Inside the envelope there is a letter";
    }

    private String showMap() {
        String map =
                "                [ Quiz Room ]\n" +
                        "                     |\n" +
                        "                     |\n" +
                        "[ Pub ] ---- [ City Centre ] ---- [ Skyscraper ]\n" +
                        "     |                                  |\n" +
                        "     |                                  |\n" +
                        " [ Toilet ] ----------------------[ Admin Office ]\n";

        return map;

    }


    private String openChest() {
        Room current = player.getCurrentRoom();
        for (Item item : current.getItems()) {
            if (item.getName().equalsIgnoreCase("Chest")) {
                Item money = new Item("Money", "A crisp €10 note.");
                player.addItem(money);
                return "You opened the chest and found €10!\nYou can now use the money to buy a shot";
            }
        }
        return "There is no chest here.";
    }


    private String buyShot() {
        Room current = player.getCurrentRoom();


        if (current.getDescription().equalsIgnoreCase("in the local pub")) {


            if (player.hasItem("Money")) {
                player.removeItem("Money");
                Item token = new Item("Token", "A token for the shot");
                player.addItem(token);
                return "You just spent €10 on a shot\nYou can now 'drink' the shot";
            } else {
                return "You don't have any money to buy a shot.";
            }


        } else {
            return "You must be in the pub to buy a shot.";
        }
    }


    private String drinkShot() {
        if (player.hasItem("Token")) {
            player.removeItem("Token");
            startShotTimer();
            ShotItem shot = new ShotItem(this);
            player.addItem(shot);
            return shot.use(player);

        } else {
            return "You must buy a shot before you can drink it.";
        }
    }

    public String getTimerMessage() {
        return timerMessage;
    }

    public void startShotTimer() {
        if (timerRunning) return;

        timerRunning = true;
        timeLeft = 60;

        beerTimerThread = new Thread(() -> {
            try {
                while (timeLeft > 0 && timerRunning) {
                    Thread.sleep(1000);
                    timeLeft--;
                    timerMessage = "Time left: " + timeLeft + " seconds";
                }

                if (timerRunning && timeLeft == 0) {
                    timerMessage = "You got sick and died!\nGAME OVER.";
                    gameOver = true;
                    timerRunning = false;
                }

            } catch (InterruptedException ignored) {
            }
        });

        beerTimerThread.setDaemon(true);
        beerTimerThread.start();
    }

    public void clearTimerMessage() {
        timerMessage = "";
    }


    private String takeItem(Command command) {
        if (!command.hasSecondWord()) {
            return "Take what?";
        }
        String itemName = command.getSecondWord();
        Room current = player.getCurrentRoom();
        Item item = current.removeItemByName(itemName);


        if (item == null) {
            return "There is no " + itemName + " here.";
        } else {
            player.addItem(item);
            return "You picked up the " + itemName + ".";
        }
    }


    public String removeItem(Command command) {
        if (!command.hasSecondWord()) {
            return "Drop what?";
        }


        String itemName = command.getSecondWord();
        Item item = player.removeItem(itemName);


        if (item == null) {
            return "You don't have a '" + itemName + "'.";
        } else {
            player.getCurrentRoom().addItem(item);
            return "You dropped the " + item.getName() + ".";
        }
    }

    private String saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("save.txt"))) {

            // Save current room
            writer.println("room=" + player.getCurrentRoom().getDescription());

            // Save inventory list
            writer.print("inventory=");
            for (Item item : player.getInventory()) {
                writer.print(item.getName() + ",");
            }
            writer.println();

            return "Game saved!";
        } catch (IOException e) {
            return "Error saving game.";
        }
    }

    private String restart() {
        gameOver = false;
        skip = true;
        createRooms();
        player = new Character("Main player", cityCentre);
        j = 0;
        s = 0;
        player.addItem(new Item("Envelope (Open Me)", "A closed envelope"));
        return printWelcome();
    }


    private void loadGame() {
        File file = new File("save.txt");
        if (!file.exists()) {
            return;  // just return quietly
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String roomLine = reader.readLine();
            String invLine = reader.readLine();

            if (roomLine != null && roomLine.startsWith("room=")) {
                String roomDesc = roomLine.substring(5);
                Room savedRoom = findRoomByDescription(roomDesc);

                if (savedRoom != null) {
                    player.setCurrentRoom(savedRoom);
                }
            }

            if (invLine != null && invLine.startsWith("inventory=")) {
                String list = invLine.substring(10);
                String[] items = list.split(",");

                for (String itemName : items) {
                    if (!itemName.isBlank()) {
                        player.addItem(new Item(itemName, "Loaded item"));
                    }
                }
            }

        } catch (IOException e) {
            // ignore for GUI
        }
    }


    private Room findRoomByDescription(String desc) {
        for (Room r : allRooms) {
            if (r.getDescription().equalsIgnoreCase(desc)) {
                return r;
            }
        }
        return null;
    }


    private String goRoom(String direction) {

        Room currentRoom = player.getCurrentRoom();
        Direction dir = Direction.fromString(direction);
        if (dir == null) {
            return "That is not a valid direction.";
        }

        Room nextRoom = currentRoom.getExit(dir.name().toLowerCase());


        if (nextRoom == null) {
            return "There is no door!";
        }

        // Quiz room
        if (nextRoom instanceof QuizRoom) {
            QuizRoom qr = (QuizRoom) nextRoom;
            GameGUIJavaFX.QuizWindow.showQuiz(qr, player);
            return "You finished the quiz!";
        }


        // Skyscraper check
        if (nextRoom.getDescription().equalsIgnoreCase("inside the skyscraper")) {

            if (!player.hasItem("shovel") || !player.hasItem("key") || !player.hasItem("flower") || !player.hasItem("shot") || !player.hasItem("certificate")) {

                return "The door to the skyscraper is locked.\nYou must collect:\n- A shovel from the toilet\n- A key hidden in the office\n- A flower from the toilet\n- A certificate from the quiz room\n- Have taken a shot from in the pub";
            }


            gameOver = true;
            return "You enter the skyscraper\nYour score is: " + s + "\nYOU WIN THE GAME!";
        }

        // Move player
        player.setCurrentRoom(nextRoom);

        // Office rules
        if (nextRoom.getDescription().equalsIgnoreCase("in the computing admin office")) {

            if (!player.hasItem("shovel")) {
                return "You must first get a shovel from the toilet before entering this room.";
            }

            if (player.hasItem("shovel")) {
                return nextRoom.getLongDescription() + "\nYou must now 'USE SHOVEL' in order to find a hidden key.";
            }
        }

        // Toilet timer
        if (nextRoom.getDescription().equalsIgnoreCase("in the toilet")) {
            if (timerRunning) {
                timerRunning = false;
                return "\nYou made it to the toilet in time!\n" + player.getCurrentRoom().getLongDescription() + "\n";
            }
        }

        return nextRoom.getLongDescription();
    }


    public String processCommandGUI(String fullCommand) {
        s++;
        String[] parts = fullCommand.split(" ");
        if (parts.length == 0) {
            return "No command entered.";
        }

        String cmd = parts[0].toLowerCase();
        String second = (parts.length > 1) ? parts[1].toLowerCase() : null;

        switch (cmd) {

            case "go":
                if (second == null) return "Go where?";
                return goRoom(second);

            case "take":
                if (second == null) return "Take what?";
                return takeItem(new Command("take", second));

            case "drop":
                if (second == null) return "Drop what?";
                return removeItem(new Command("drop", second));

            case "open":
                if (second == null) return "Open what?";
                if (second.equals("chest")) return openChest();
                if (second.equals("envelope")) return openEnvelope();
                return "You cannot open that.";

            case "read":
                if (second == null) return "Read what?";
                return readLetter(new Command("read", second));

            case "inventory":
                return player.getInventoryString();

            case "use":
                if (second == null) return "Use what?";

                for (Item item : player.getInventory()) {
                    if (item.getName().equalsIgnoreCase(second)) {

                        if (item instanceof Usable usable) {
                            return usable.use(player);  // POLYMORPHISM
                        }

                        return "You cannot use that.";
                    }
                }
                return "You do not have that item.";


            case "buy":
                return buyShot();

            case "drink":
                return drinkShot();

            case "map":
                return showMap();

            case "restart":
                return restart();

            case "help":
                return printHelp();

            case "save":
                return saveGame();

            default:
                return "Unknown command: " + cmd;
        }
    }
}








