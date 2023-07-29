package htw.berlin.de.material_system.serviceImpl;

import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.repository.CardRepository;
import org.junit.jupiter.api.*;

import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.serviceImpl.CardServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * The type Card service test.
 */
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        cardService = new CardServiceImpl(cardRepository);
    }

    /**
     * It verifies that a card is correctly created.
     */
    @Test
    public void testCreateCard_ShouldReturnCorrectCard() {
        // Arrange
        Suits suit = Suits.HEART;
        Ranks rank = Ranks.ACE;
        Card card = new Card(1L, suit, rank);

        // Act
        Mockito.when(cardRepository.save(any())).thenReturn(card);
        Card cardExpected = cardService.createCard(suit, rank);


        // Assert
        Assertions.assertEquals(suit, cardExpected.getSuit());
        Assertions.assertEquals(rank, cardExpected.getRank());
    }

    /**
     * It verifies that the card is converted to its string representation
     * correctly.
     */
    @Test
    public void testGetCardAsString_ShouldReturnCorrectCard() {
        // Arrange
        Card card = new Card(1L, Suits.DIAMOND, Ranks.ACE);

        // Act
        String result = cardService.getCardAsString(card);

        // Assert
        Assertions.assertEquals(result, "DIAMOND ACE");
    }

    /**
     * It verifies that a card is correctly created.
     */
    @Test
    public void testGetCardAsString_ShouldReturnEmptyResult() {
        // Arrange
        Card nullCard = new Card(null, null, null);

        // Act
        String result = cardService.getCardAsString(nullCard);

        // Assert
        Assertions.assertFalse(result.isEmpty());
    }

    /**
     * It verifies that the correct card is retrieved by its ID.
     *
     * @throws CardNotFoundException if the card with the given ID does not exist
     */
    @Test
    public void testGetCardById_ShouldReturnCorrectCard() throws CardNotFoundException {
        // Arrange
        Card card = new Card(1L, Suits.CLUB, Ranks.QUEEN);
        Mockito.when(cardRepository.findById(1L)).thenReturn(java.util.Optional.of(card));

        // Act
        Card result = cardService.getCardById(card.getId());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(card.getId(), result.getId());
    }

    /**
     * It verifies that the correct card is retrieved by its ID.
     *
     * @throws CardNotFoundException if the card with the given ID does not exist
     */
    @Test
    public void testGetCardById_ShouldThrowException() {
        // Arrange
        Long nonExistingCardId = 100L;

        // Assert
        Assertions.assertThrows(CardNotFoundException.class, () -> {
            // Act
            cardService.getCardById(nonExistingCardId);
        });
    }

    /**
     * It verifies that the card is updated correctly.
     *
     * @throws CardNotFoundException if the card with the given ID does not exist
     */
    @Test
    public void testDeleteCard_ShouldThrowException() throws CardNotFoundException {
        // Arrange
        Card card = new Card(1L, Suits.CLUB, Ranks.TEN);

        // Act
        cardService.deleteCard(card);

        // Assert
        Assertions.assertThrows(CardNotFoundException.class, () -> {
            cardService.getCardById(card.getId());
        });
    }
}