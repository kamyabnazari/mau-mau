package htw.berlin.de.material_system.entity;

import org.junit.jupiter.api.*;

public class SuitsTest {

    @Test
    public void testSuitsValues() {
        Suits[] suits = Suits.values();

        Assertions.assertEquals(4, suits.length);
        Assertions.assertArrayEquals(new Suits[] { Suits.HEART, Suits.DIAMOND, Suits.CLUB, Suits.SPADE }, suits);
    }

    @Test
    public void testEnumToString() {
        Assertions.assertEquals("HEART", Suits.HEART.toString());
        Assertions.assertEquals("CLUB", Suits.CLUB.toString());
        Assertions.assertEquals("DIAMOND", Suits.DIAMOND.toString());
        Assertions.assertEquals("SPADE", Suits.SPADE.toString());
    }

    @Test
    public void testEnumValueOf() {
        Assertions.assertEquals(Suits.HEART, Suits.valueOf("HEART"));
        Assertions.assertEquals(Suits.CLUB, Suits.valueOf("CLUB"));
        Assertions.assertEquals(Suits.DIAMOND, Suits.valueOf("DIAMOND"));
        Assertions.assertEquals(Suits.SPADE, Suits.valueOf("SPADE"));
    }
}
