package htw.berlin.de.game_manager.entity;

import java.util.ArrayList;
import java.util.List;

import htw.berlin.de.material_system.entity.PileOfCards;
import org.junit.jupiter.api.*;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.player_manager.entity.Player;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        List<Card> deckCards = new ArrayList<>();
        deckCards.add(new Card(1L, Suits.CLUB, Ranks.FOUR));
        deckCards.add(new Card(2L, Suits.CLUB, Ranks.FIVE));
        deckCards.add(new Card(3L, Suits.CLUB, Ranks.SIX));
        PileOfCards deck = new PileOfCards(1L, deckCards);

        List<Card> discardPileCards = new ArrayList<>();
        discardPileCards.add(new Card(1L, Suits.CLUB, Ranks.SEVEN));
        PileOfCards discardPile = new PileOfCards(1L, discardPileCards);

        game = new Game(1L, Directions.CLOCKWISE, Suits.CLUB, deck, discardPile);
    }

    @Test
    public void testGetId() {
        Assertions.assertEquals(1L, game.getId());
    }

    @Test
    public void testSetId() {
        game.setId(2L);
        Assertions.assertEquals(2L, game.getId());
    }

    @Test
    public void testGetDirection() {
        Assertions.assertEquals(Directions.CLOCKWISE, game.getDirection());
    }

    @Test
    public void testSetDirection() {
        game.setDirection(Directions.COUNTERCLOCKWISE);
        Assertions.assertEquals(Directions.COUNTERCLOCKWISE, game.getDirection());
    }

    @Test
    public void testGetSuit() {
        Assertions.assertEquals(Suits.CLUB, game.getSuit());
    }

    @Test
    public void testSetSuit() {
        game.setSuit(Suits.DIAMOND);
        Assertions.assertEquals(Suits.DIAMOND, game.getSuit());
    }

    @Test
    public void testGetDeck() {
        Assertions.assertEquals(1L, game.getDeck().getId());
    }

    @Test
    public void testSetDeck() {
        List<Card> deckCards = new ArrayList<>();
        deckCards.add(new Card(1L, Suits.CLUB, Ranks.FOUR));
        deckCards.add(new Card(2L, Suits.CLUB, Ranks.FIVE));
        deckCards.add(new Card(3L, Suits.CLUB, Ranks.SIX));
        PileOfCards deck = new PileOfCards(2L, deckCards);
        game.setDeck(deck);
        Assertions.assertEquals(2L, game.getDeck().getId());
    }

    @Test
    public void testGetDiscardPile() {
        Assertions.assertEquals(1L, game.getDiscardPile().getId());
    }

    @Test
    public void testSetDiscardPile() {
        List<Card> discardPileCards = new ArrayList<>();
        discardPileCards.add(new Card(4L, Suits.CLUB, Ranks.SEVEN));
        PileOfCards discardPile = new PileOfCards(2L, discardPileCards);
        game.setDiscardPile(discardPile);
        Assertions.assertEquals(2L, game.getDiscardPile().getId());
    }

    @Test
    public void testGetPlayerList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        Assertions.assertEquals(1L, game.getPlayerList().get(0).getId());
        Assertions.assertEquals("James", game.getPlayerList().get(0).getName());
        Assertions.assertEquals(2L, game.getPlayerList().get(1).getId());
        Assertions.assertEquals("Mark", game.getPlayerList().get(1).getName());
        Assertions.assertEquals(2, game.getPlayerList().size());
    }

    @Test
    public void testSetPlayerList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        Assertions.assertEquals(1L, game.getPlayerList().get(0).getId());
        Assertions.assertEquals("James", game.getPlayerList().get(0).getName());
        Assertions.assertEquals(2L, game.getPlayerList().get(1).getId());
        Assertions.assertEquals("Mark", game.getPlayerList().get(1).getName());
        Assertions.assertEquals(2, game.getPlayerList().size());
    }

    @Test
    public void testIsRunning() {
        game.setRunning(false);
        Assertions.assertFalse(game.isRunning());
    }

    @Test
    public void testSetRunning() {
        game.setRunning(true);
        Assertions.assertTrue(game.isRunning());
    }

    @Test
    public void testGetCurrentPlayer() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        // Act
        game.setCurrentPlayer(playerList.get(0));

        // Assert
        Assertions.assertEquals(1L, game.getCurrentPlayer().getId());
        Assertions.assertEquals("James", game.getCurrentPlayer().getName());
    }

    @Test
    public void testSetCurrentPlayer() {
        // Arrange
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        // Act
        game.setCurrentPlayer(playerList.get(1));

        // Assert
        Assertions.assertEquals(2L, game.getCurrentPlayer().getId());
        Assertions.assertEquals("Mark", game.getCurrentPlayer().getName());
    }

    @Test
    public void testGetNextPlayer() {
        // Arrange
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        // Act
        game.setCurrentPlayer(playerList.get(0));
        game.setNextPlayer(playerList.get(1));

        // Assert
        Assertions.assertEquals(2L, game.getNextPlayer().getId());
        Assertions.assertEquals("Mark", game.getNextPlayer().getName());
    }

    @Test
    public void testSetNextPlayer() {
        // Arrange
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(1L, "James"));
        playerList.add(new Player(2L, "Mark"));

        // Add playerList to game
        game.setPlayerList(playerList);

        // Act
        game.setCurrentPlayer(playerList.get(1));
        game.setNextPlayer(playerList.get(0));

        // Assert
        Assertions.assertEquals(1L, game.getNextPlayer().getId());
        Assertions.assertEquals("James", game.getNextPlayer().getName());
    }
}
