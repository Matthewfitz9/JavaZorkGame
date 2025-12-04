//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Scanner;

public class Parser {
    private CommandWords commands = new CommandWords();
    private Scanner reader;

    public Parser() {
        this.reader = new Scanner(System.in);
    }

    public Command getCommand() {
        System.out.print("> ");
        String inputLine = this.reader.nextLine();
        String word1 = null;
        String word2 = null;
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }

        return this.commands.isCommand(word1) ? new Command(word1, word2) : new Command((String)null, word2);
    }

    public Command parseCommand(String inputLine) {
        String word1 = null;
        String word2 = null;

        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.nextLine().trim();
            }
        }

        if (!commands.isCommand(word1)) {
            return new Command(null, word2);
        }

        return new Command(word1, word2);
    }
}
