//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Command {
    private String commandWord;
    private String secondWord;

    public Command(String firstWord, String secondWord) {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
    }

    public String getCommandWord() {
        return this.commandWord;
    }

    public String getSecondWord() {
        return this.secondWord;
    }

    public boolean hasSecondWord() {
        return this.secondWord != null;
    }
}
