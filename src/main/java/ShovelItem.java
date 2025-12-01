public class ShovelItem extends Item implements Usable {

    public ShovelItem() {
        super("Shovel", "A sturdy shovel");
    }

    @Override
    public String use(Character player) {
        Item key = new Item("Key", "A key found while digging!");
        player.addItem(key);
        return "You dig with the shovel and uncover a key!";
    }
}
