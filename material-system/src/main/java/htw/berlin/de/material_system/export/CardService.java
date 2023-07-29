package htw.berlin.de.material_system.export;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;

import java.util.List;

/**
 * The interface for methods which only involves one card.
 */
public interface CardService {

    /**
     * Creates a new card with the specified suit and rank.
     *
     * @param suit the suit of the card
     * @param rank the rank of the card
     * @return the created card object
     */
    Card createCard(Suits suit, Ranks rank);

    /**
     * Gets card as a string.
     *
     * @param card the card
     * @return the card as string
     */
    String getCardAsString(Card card);

    /**
     * Retrieves a card by its ID.
     *
     * @param id the ID of the card
     * @return the card object
     * @throws CardNotFoundException if the card with the given ID does not exist
     */
    Card getCardById(Long id) throws CardNotFoundException;

    /**
     * Updates the suit and rank of a card.
     *
     * @param cardId the id of the card to be updated
     * @param order order of the card in its new pile of cards
     *
     */
    void updateCard(Long cardId, int order) throws CardNotFoundException;

    /**
     * Deletes a card.
     *
     * @param card the card to delete
     */
    void deleteCard(Card card) throws CardNotFoundException;

    PileOfCards saveCardsInPileOfCard(PileOfCards pile, List<Card> cardList);
}

