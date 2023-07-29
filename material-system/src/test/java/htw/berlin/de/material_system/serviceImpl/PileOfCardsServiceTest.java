package htw.berlin.de.material_system.serviceImpl;

import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.material_system.entity.*;
import htw.berlin.de.material_system.export.PileOfCardsService;
import htw.berlin.de.material_system.repository.PileOfCardsRepository;

import org.junit.jupiter.api.Assertions;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * The type Deck service test.
 */
@ExtendWith(MockitoExtension.class)
public class PileOfCardsServiceTest {

    private PileOfCardsService pileOfCardsService;
    @Mock
    private PileOfCardsRepository pileOfCardsRepository;
    @Mock
    private CardService cardService;


    @BeforeEach
    public void setUp() {
        pileOfCardsService = new PileOfCardsServiceImpl(pileOfCardsRepository, cardService);
    }

    /**
     * It verifies that deck is shuffled.
     */
    @Test
    public void testShuffle_ShouldReturnShuffledDeck() {
        // Arrange
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(1L, Suits.SPADE, Ranks.ACE));
        deck.add(new Card(2L, Suits.HEART, Ranks.TWO));
        deck.add(new Card(3L, Suits.DIAMOND, Ranks.THREE));
        deck.add(new Card(4L, Suits.CLUB, Ranks.FOUR));
        List<Card> original = new ArrayList<>(deck);

        PileOfCards pile = new PileOfCards(1L, deck);

        // Act
        PileOfCards shuffledDeck = pileOfCardsService.shuffle(pile);

        // Assert
        Assertions.assertNotNull(shuffledDeck);
        Assertions.assertEquals(original.size(), shuffledDeck.getListOfCard().size());
        Assertions.assertNotEquals(original, shuffledDeck);
    }

//
    /**
     * It verifies that a new deck is created.
     */
    @Test
    public void testGetNewDeck_ShouldUseCardService() throws CardNotFoundException {
        // Arrange
        PileOfCards newDeck = new PileOfCards();

        // Act
        Mockito.when(pileOfCardsRepository.save(any())).thenReturn(newDeck);
        PileOfCards deck = pileOfCardsService.getNewDeck();

        // Assert
        verify(cardService, times(52)).getCardById(any());
    }


    @Test
    public void refillDeck_ShouldBehaveAsExpected() {
        // Arrange
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        PileOfCards fullDiscardPile = new PileOfCards(1L, Arrays.asList(card1, card2, card3, card4));
        PileOfCards emptyDeck = new PileOfCards(2L, new ArrayList<>());


        // Act
        pileOfCardsService.refillDeck(emptyDeck, fullDiscardPile);

        // Assert
        Assertions.assertEquals(3, emptyDeck.getListOfCard().size());
        Assertions.assertEquals(1, fullDiscardPile.getListOfCard().size());
    }

    @Test
    public void drawCards() throws EmptyPileOfCardsException {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        PileOfCards deck = new PileOfCards(1L, Arrays.asList(card1, card2, card3, card4));

        pileOfCardsService.drawCards(deck, 2);

        Assertions.assertEquals(2, deck.getListOfCard().size());
    }

    @Test
    void getTopCard() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        PileOfCards deck = new PileOfCards(1L, Arrays.asList(card1, card2, card3, card4));

        Card topCard = pileOfCardsService.getTopCard(deck);

        Assertions.assertEquals(4L, topCard.getId());
    }

    @Test
    void addOneCard() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        Card addCard = new Card(5L, Suits.CLUB, Ranks.KING);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        PileOfCards deck = new PileOfCards(1L, cardList);

        PileOfCards newDeck = pileOfCardsService.addOneCard(deck, addCard);
        Card topCardOfNewDeck = newDeck.getListOfCard().get(newDeck.getListOfCard().size()-1);

        Assertions.assertEquals(5L, topCardOfNewDeck.getId());
    }

    @Test
    void addSeveralCards() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        PileOfCards deck = new PileOfCards(1L, cardList);

        pileOfCardsService.addSeveralCards(deck, cardList);

        Assertions.assertEquals(8, deck.getListOfCard().size());
    }

    @Test
    void sortCards() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        Card card5 = new Card(5L, Suits.HEART, Ranks.QUEEN);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        PileOfCards deck = new PileOfCards(1L, cardList);

        pileOfCardsService.sortCards(deck);

        Assertions.assertEquals(deck.getListOfCard().get(0), card1);
        Assertions.assertEquals(deck.getListOfCard().get(1), card2);
        Assertions.assertEquals(deck.getListOfCard().get(2), card4);
        Assertions.assertEquals(deck.getListOfCard().get(3), card5);
        Assertions.assertEquals(deck.getListOfCard().get(4), card3);
    }
}