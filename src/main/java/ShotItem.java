public class ShotItem extends Item implements Usable {

    private ZorkULGame game;

    public ShotItem(ZorkULGame game) {
        super("Shot", "A cold, refreshing shot.");
        this.game = game;
    }

    @Override
    public String use(Character player) {
        game.startShotTimer();
        return "You drink the shot. You now have 1 minute to reach the toilet!";
    }
}
