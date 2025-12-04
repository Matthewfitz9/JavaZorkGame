//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Item {
    private String description;
    private String name;
    private boolean isVisible;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.isVisible = true;
    }


    public String getName() {
        return this.name;
    }
}
