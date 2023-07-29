package htw.berlin.de.game_rules.serviceImpl;

import htw.berlin.de.game_rules.export.GameRulesService;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.export.PileOfCardsService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;

import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.game_rules.serviceImpl.GameRulesServiceImpl;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test for RulesService.
 */
@ExtendWith(MockitoExtension.class)
public class GameRulesServiceTest {

    private GameRulesService service;
    @Mock
    private PileOfCardsService pileOfCardsService;

    @BeforeEach
    public void setUp() {
        GameRulesServiceImpl service = new GameRulesServiceImpl(pileOfCardsService);
        this.service = service;
    }

    @Test
    public void testGetTopAccumulativeCardsPlayed_ShouldReturnZeroBecauseSevenWasNotOnTop() throws InvalidRuleException {
        // Arrange
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(1L, Suits.CLUB, Ranks.SEVEN));
        cards.add( new Card(2L, Suits.CLUB, Ranks.SIX));
        PileOfCards discardPile = new PileOfCards(1L, cards);

        // Act
        int result = service.getTopAccumulativeCardsPlayed(discardPile);

        // Assert
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testGetTopAccumulativeCardsPlayed_ShouldReturnNumberOfAccumulativeOne() throws InvalidRuleException {
        // Arrange
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add( new Card(2L, Suits.CLUB, Ranks.SIX));
        cards.add(new Card(1L, Suits.CLUB, Ranks.SEVEN));
        PileOfCards discardPile = new PileOfCards(1L, cards);

        // Act
        int result = service.getTopAccumulativeCardsPlayed(discardPile);

        // Assert
        Assertions.assertEquals(1, result);
    }


    @Test
    public void testGetTopAccumulativeCardsPlayed_ShouldReturnNumberOfAccumulativeTwo() throws InvalidRuleException {
        // Arrange
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add( new Card(2L, Suits.CLUB, Ranks.SEVEN));
        cards.add(new Card(1L, Suits.CLUB, Ranks.SEVEN));
        PileOfCards discardPile = new PileOfCards(1L, cards);

        // Act
        int result = service.getTopAccumulativeCardsPlayed(discardPile);

        // Assert
        Assertions.assertEquals(2, result);
    }

    @Test
    public void testGetTopAccumulativeCardsPlayed_ShouldReturnZero() throws InvalidRuleException {
        // Arrange
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(1L, Suits.CLUB, Ranks.FIVE));
        cards.add(new Card(2L, Suits.SPADE, Ranks.FOUR));

        PileOfCards discardPile = new PileOfCards(1L, cards);

        //Act
        int result = service.getTopAccumulativeCardsPlayed(discardPile);

        //Assert
        Assertions.assertEquals(0, result);
    }

}
