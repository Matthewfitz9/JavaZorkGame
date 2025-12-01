import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZorkULGameTest {

    @Test
    void testStartingRoom() {
        ZorkULGame game = new ZorkULGame();
        String roomDesc = game.getPlayer().getCurrentRoom().getDescription();

        assertEquals("outside in the city centre", roomDesc);
    }

    @Test
    void testOpenEnvelopeAllowsReading() {
        ZorkULGame game = new ZorkULGame();

        // Before opening, reading should fail
        String result1 = game.processCommandGUI("read letter");
        assertEquals("You must open the envelope before reading it.", result1);

        // After opening, reading works
        game.processCommandGUI("open envelope");
        String result2 = game.processCommandGUI("read letter");

        assertTrue(result2.contains("navigate around the map"));
    }

    @Test
    void testTakingItem() {
        ZorkULGame game = new ZorkULGame();

        // Move to toilet where shovel is
        game.processCommandGUI("go south"); // pub
        game.processCommandGUI("go south"); // toilet

        String result = game.processCommandGUI("take shovel");

        assertEquals("You picked up the shovel.", result);
        assertTrue(game.getPlayer().hasItem("shovel"));
    }

    @Test
    void testCannotEnterSkyscraperWithoutItems() {
        ZorkULGame game = new ZorkULGame();

        String response = game.processCommandGUI("go east");

        assertTrue(response.contains("locked"));
        assertTrue(response.contains("You must collect"));
    }

    @Test
    void testBuyShot() {
        ZorkULGame game = new ZorkULGame();

        // Go to pub (west)
        game.processCommandGUI("go west");

        // Make money appear in inventory for testing
        game.getPlayer().addItem(new Item("Money", "Test money"));

        String output = game.processCommandGUI("buy");

        assertTrue(output.contains("spent €10"));
        assertTrue(game.getPlayer().hasItem("Token"));
    }

    @Test
    void testDrinkShotStartsTimer() {
        ZorkULGame game = new ZorkULGame();

        // Give token so they can drink
        game.getPlayer().addItem(new Item("Token", "Test token"));

        String output = game.processCommandGUI("drink");

        assertTrue(output.contains("1 minute"), "Timer should start when drinking shot");
    }
}
