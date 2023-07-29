package htw.berlin.de.game_manager.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import htw.berlin.de.game_manager.entity.Game;
import htw.berlin.de.game_manager.export.GameService;
import htw.berlin.de.game_rules.export.GameRulesService;
import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.player_manager.export.PlayerService;
import org.junit.jupiter.api.*;

import htw.berlin.de.game_manager.entity.Directions;
import htw.berlin.de.game_manager.repository.GameRepository;
import htw.berlin.de.game_manager.serviceImpl.GameServiceImpl;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.PileOfCardsService;
import htw.berlin.de.player_manager.entity.Player;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private GameService gameService;

    @Mock
    GameRepository gameRepository;
    @Mock
    GameRulesService gameRulesService;
    @Mock
    PlayerService playerService;
    @Mock
    PileOfCardsService pileOfCardsService;
    @Mock
    CardService cardService;
    Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
    Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
    Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
    Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
    PileOfCards deck = new PileOfCards(1L, Arrays.asList(card1, card2, card3, card4));
    PileOfCards discardPile = new PileOfCards(2L, new ArrayList<>());

    @BeforeEach
    void setUp(){
        gameService = new GameServiceImpl(gameRepository, pileOfCardsService, playerService, gameRulesService, cardService);
    }


    @Test
    public void testInstantiateGame_ShouldNotThrowException() throws Exception {
        Mockito.when(pileOfCardsService.getNewDeck()).thenReturn(deck);
        Mockito.when(pileOfCardsService.getNewDiscardPile()).thenReturn(discardPile);
        Player player1 = new Player(1L, "anh1");
        Player player2 = new Player(2L, "anh2");
        List<Player> playerList = Arrays.asList(player1, player2);
        Mockito.when(pileOfCardsService.drawCards(any(), eq(1))).thenReturn(Arrays.asList(card1));
        Assertions.assertDoesNotThrow(() -> gameService.instantiateGame(playerList));
    }

//    @Test
//    public void testEndGame_ShouldNotThrowException() throws Exception {
//        // Arrange
//        Player player = new Player(1L, "James");
//        Game game = new Game(1L, Directions.CLOCKWISE, Suits.HEART, deck, discardPile);
//        // Act
//        Assertions.assertFalse(gameService.endGame(game,player));
//        Assertions.assertTrue(game.isRunning());
//    }

    @Test
    public void testChangeSuit() {
        // Act
        Game game = new Game(1L, Directions.CLOCKWISE, Suits.HEART, deck, discardPile);
        Assertions.assertDoesNotThrow(() -> gameService.changeSuit(game, Suits.CLUB));
        Assertions.assertEquals(game.getSuit(), Suits.CLUB);
    }


    @Test
    public void testGetNextPlayer_ShouldReturnCorrectIndex0(){
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        List<Player> playerList = Arrays.asList(player1, player2, player3);
        Game game = new Game();
        game.setPlayerList(playerList);
        game.setCurrentPlayer(player3);
        Assertions.assertEquals(0, gameService.getNextPlayersIndex(game));
    }

    @Test
    public void testGetNextPlayer_ShouldReturnCorrectIndex(){
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        List<Player> playerList = Arrays.asList(player1, player2, player3);
        Game game = new Game();
        game.setPlayerList(playerList);
        game.setCurrentPlayer(player2);
        Assertions.assertEquals(2, gameService.getNextPlayersIndex(game));
    }
}
