package htw.berlin.de.player_manager.serviceImpl;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.PileOfCardsService;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import htw.berlin.de.player_manager.entity.Player;
import htw.berlin.de.player_manager.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PileOfCardsService pileOfCardsService;

    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerServiceImpl(playerRepository, pileOfCardsService);
    }
    /**
     * Test case to verify that a player is created and saved successfully.
     */
    @Test
    void createPlayer_ShouldSavePlayer() {
        // Arrange
        String playerName = "John Doe";
        Player newPlayer = new Player(playerName);
        when(playerRepository.save(any(Player.class))).thenReturn(newPlayer);

        // Act
        Player result = playerService.createPlayer(playerName);

        // Assert
        assertNotNull(result);
        assertEquals(playerName, result.getName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }
    /**
     * Test case to verify that an existing player is retrieved successfully by ID.
     *
     * @throws PlayerNotFoundException if the player with the given ID does not exist.
     */
    @Test
    void getPlayerById_ExistingPlayer_ShouldReturnPlayer() throws PlayerNotFoundException {
        // Arrange
        Long playerId = 1L;
        Player existingPlayer = new Player("Mari Dan");
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));

        // Act
        Player result = playerService.getPlayerById(playerId);

        // Assert
        assertNotNull(result);
        assertEquals(existingPlayer, result);
        verify(playerRepository, times(1)).findById(playerId);
    }
    /**
     * Test case to verify that an exception is thrown when trying to retrieve a non-existing player by ID.
     */
    @Test
    void getPlayerById_NonExistingPlayer_ShouldThrowException() {
        // Arrange
        Long playerId = 1L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById(playerId));
        verify(playerRepository, times(1)).findById(playerId);
    }
    /**
     * Test case to verify that an existing player is replaced and saved successfully.
     *
     * * @throws PlayerNotFoundException if the player to be replaced does not exist.
     *  */
    @Test
    void replacePlayer_ExistingPlayer_ShouldReplaceAndSavePlayer() throws PlayerNotFoundException {
        // Arrange
        Player existingPlayer = new Player("Mari Dan");
        Player updatedPlayer = new Player("Jean Don");
        when(playerRepository.findById(existingPlayer.getId())).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);

        // Act
        Player result = playerService.replacePlayer(updatedPlayer);

        // Assert
        assertNotNull(result);
        assertEquals(updatedPlayer, result);
        verify(playerRepository, times(1)).findById(existingPlayer.getId());
        verify(playerRepository, times(1)).delete(existingPlayer);
        verify(playerRepository, times(1)).save(updatedPlayer);
    }
    /**
     * Test case to verify that an exception is thrown when trying to replace a non-existing player.
     */
    @Test
    void replacePlayer_NonExistingPlayer_ShouldThrowException() {
        // Arrange
        Player nonExistingPlayer = new Player("Mari Dan");
        when(playerRepository.findById(nonExistingPlayer.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.replacePlayer(nonExistingPlayer));
        verify(playerRepository, times(1)).findById(nonExistingPlayer.getId());
        verify(playerRepository, never()).delete(any(Player.class));
        verify(playerRepository, never()).save(any(Player.class));
    }
    /**
     * Test case to verify that a player is deleted successfully.
     */
    @Test
    void deletePlayer_ShouldDeletePlayer() {
        // Arrange
        Player player = new Player("Mari Dan");

        // Act
        playerService.deletePlayer(player);

        // Assert
        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    void addOneCardToHand() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        Card newCard = new Card(5L, Suits.HEART, Ranks.QUEEN);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        PileOfCards hand = new PileOfCards(1L, cardList);

        Player player = new Player();
        player.setHand(hand);
        cardList.add(newCard);

        Mockito.when(pileOfCardsService.addOneCard(any(), any())).thenReturn(hand);
        playerService.addOneCardToHand(player, newCard);

        assertEquals(5, player.getHand().getListOfCard().size());
    }

    @Test
    void addManyCardsToHand() {
        Card card1 = new Card(1L, Suits.DIAMOND, Ranks.ACE);
        Card card2 = new Card(2L, Suits.HEART, Ranks.FOUR);
        Card card3 = new Card(3L, Suits.CLUB, Ranks.QUEEN);
        Card card4 = new Card(4L, Suits.SPADE, Ranks.FIVE);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        PileOfCards hand = new PileOfCards(1L, cardList);

        Player player = new Player();
        player.setHand(hand);

        Mockito.when(pileOfCardsService.addSeveralCards(any(), any())).thenReturn(hand);
        playerService.addManyCardsToHand(player, cardList);

        assertEquals(4, player.getHand().getListOfCard().size());
    }
}
