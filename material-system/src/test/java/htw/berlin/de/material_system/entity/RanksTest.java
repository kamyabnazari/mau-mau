package htw.berlin.de.material_system.entity;

import org.junit.jupiter.api.*;

public class RanksTest {

    @Test
    public void testRanksValues() {
        Ranks[] ranks = Ranks.values();

        Assertions.assertEquals(13, ranks.length);
        Assertions.assertArrayEquals(new Ranks[] { Ranks.ACE, Ranks.TWO, Ranks.THREE, Ranks.FOUR, Ranks.FIVE, Ranks.SIX,
                Ranks.SEVEN, Ranks.EIGHT, Ranks.NINE, Ranks.TEN, Ranks.JACK, Ranks.QUEEN, Ranks.KING }, ranks);
    }

    @Test
    public void testEnumToString() {
        Assertions.assertEquals("ACE", Ranks.ACE.toString());
        Assertions.assertEquals("TWO", Ranks.TWO.toString());
        Assertions.assertEquals("THREE", Ranks.THREE.toString());
        Assertions.assertEquals("FOUR", Ranks.FOUR.toString());
        Assertions.assertEquals("FIVE", Ranks.FIVE.toString());
        Assertions.assertEquals("SIX", Ranks.SIX.toString());
        Assertions.assertEquals("SEVEN", Ranks.SEVEN.toString());
        Assertions.assertEquals("EIGHT", Ranks.EIGHT.toString());
        Assertions.assertEquals("NINE", Ranks.NINE.toString());
        Assertions.assertEquals("TEN", Ranks.TEN.toString());
        Assertions.assertEquals("JACK", Ranks.JACK.toString());
        Assertions.assertEquals("QUEEN", Ranks.QUEEN.toString());
        Assertions.assertEquals("KING", Ranks.KING.toString());
    }

    @Test
    public void testEnumValueOf() {
        Assertions.assertEquals(Ranks.ACE, Ranks.valueOf("ACE"));
        Assertions.assertEquals(Ranks.TWO, Ranks.valueOf("TWO"));
        Assertions.assertEquals(Ranks.THREE, Ranks.valueOf("THREE"));
        Assertions.assertEquals(Ranks.FOUR, Ranks.valueOf("FOUR"));
        Assertions.assertEquals(Ranks.FIVE, Ranks.valueOf("FIVE"));
        Assertions.assertEquals(Ranks.SIX, Ranks.valueOf("SIX"));
        Assertions.assertEquals(Ranks.SEVEN, Ranks.valueOf("SEVEN"));
        Assertions.assertEquals(Ranks.EIGHT, Ranks.valueOf("EIGHT"));
        Assertions.assertEquals(Ranks.NINE, Ranks.valueOf("NINE"));
        Assertions.assertEquals(Ranks.TEN, Ranks.valueOf("TEN"));
        Assertions.assertEquals(Ranks.JACK, Ranks.valueOf("JACK"));
        Assertions.assertEquals(Ranks.QUEEN, Ranks.valueOf("QUEEN"));
        Assertions.assertEquals(Ranks.KING, Ranks.valueOf("KING"));
    }
}
