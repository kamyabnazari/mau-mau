package htw.berlin.de.game_manager.entity;

import org.junit.jupiter.api.*;

public class DirectionTest {

    @Test
    public void testDirectionValues() {
        Directions[] directions = Directions.values();

        Assertions.assertEquals(2, directions.length);
        Assertions.assertArrayEquals(new Directions[] { Directions.CLOCKWISE, Directions.COUNTERCLOCKWISE },
                directions);
    }

    @Test
    public void testEnumToString() {
        Assertions.assertEquals("CLOCKWISE", Directions.CLOCKWISE.toString());
        Assertions.assertEquals("COUNTERCLOCKWISE", Directions.COUNTERCLOCKWISE.toString());
    }

    @Test
    public void testEnumValueOf() {
        Assertions.assertEquals(Directions.CLOCKWISE, Directions.valueOf("CLOCKWISE"));
        Assertions.assertEquals(Directions.COUNTERCLOCKWISE, Directions.valueOf("COUNTERCLOCKWISE"));
    }
}
