package htw.berlin.de.material_system.entity;

import org.junit.jupiter.api.*;

public class CardTest {

    private Card card;

    @BeforeEach
    public void setUp() {
        card = new Card(1L, Suits.CLUB, Ranks.FOUR);
    }

    @Test
    public void testGetId() {
        Assertions.assertEquals(1L, card.getId());
    }

    @Test
    public void testSetId() {
        card.setId(2L);
        Assertions.assertEquals(2L, card.getId());
    }

    @Test
    public void testGetSuit() {
        Assertions.assertEquals(Suits.CLUB, card.getSuit());
    }

    @Test
    public void testSetSuit() {
        card.setSuit(Suits.DIAMOND);
        Assertions.assertEquals(Suits.DIAMOND, card.getSuit());
    }

    @Test
    public void testGetRank() {
        Assertions.assertEquals(Ranks.FOUR, card.getRank());
    }

    @Test
    public void testSetRank() {
        card.setRank(Ranks.FIVE);
        Assertions.assertEquals(Ranks.FIVE, card.getRank());
    }
}
