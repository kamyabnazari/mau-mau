package htw.berlin.de.player_manager.entity;

import org.junit.jupiter.api.*;

/**
 * PlayerTest
 */
public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Max Mustermann");
    }

    @Test
    public void testGetId() {
        Assertions.assertEquals(null, player.getId());
    }

    @Test
    public void testSetId() {
        player.setId(2L);
        Assertions.assertEquals(2L, player.getId());
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("Max Mustermann", player.getName());
    }

    @Test
    public void testSetName() {
        player.setName("Max Mustermann Changed");
        Assertions.assertEquals("Max Mustermann Changed", player.getName());
    }
}
