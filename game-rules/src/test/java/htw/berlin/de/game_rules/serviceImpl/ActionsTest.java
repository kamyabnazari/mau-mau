package htw.berlin.de.game_rules.serviceImpl;

import htw.berlin.de.game_rules.serviceImpl.Actions;
import org.junit.jupiter.api.*;

/**
 * Unit test for Actions.
 */
public class ActionsTest {

    @Test
    public void testEnumToString() {
        Assertions.assertEquals("DRAW_ACCUMULATIVE", Actions.DRAW_ACCUMULATIVE.toString());
        Assertions.assertEquals("CHANGE_SUIT", Actions.CHANGE_SUIT.toString());
        Assertions.assertEquals("DRAW", Actions.DRAW.toString());
        Assertions.assertEquals("PLAY", Actions.PLAY.toString());
    }

    @Test
    public void testEnumValueOf() {
        Assertions.assertEquals(Actions.DRAW_ACCUMULATIVE, Actions.valueOf("DRAW_ACCUMULATIVE"));
        Assertions.assertEquals(Actions.CHANGE_SUIT, Actions.valueOf("CHANGE_SUIT"));
        Assertions.assertEquals(Actions.DRAW, Actions.valueOf("DRAW"));
        Assertions.assertEquals(Actions.PLAY, Actions.valueOf("PLAY"));
    }
}