package htw.berlin.de.material_system.export;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;

import java.util.List;

public interface PileOfCardsService {
    /**
     * creates a new deck for the game
     *
     * @return the new deck
     */
    PileOfCards getNewDeck() throws CardNotFoundException;

    /**
     * Shuffles the game's deck of cards.
     *
     * @param deck the deck to be shuffled
     * @return shuffled deck of cards
     */
    PileOfCards shuffle(PileOfCards deck);

    /**
     * Refills the deck with shuffled cards from the discard pile and clears the
     * discard pile except for the top card.
     *
     * @param deck        the deck to be refilled
     * @param discardPile the discard pile to be emptied except for the top card
     */
    void refillDeck(PileOfCards deck, PileOfCards discardPile);

    /**
     * @param deck deck that cards are drawn from
     * @param numberOfCards number of cards to be drawn
     * @return list of drawn cards
     */
    List<Card> drawCards(PileOfCards deck, int numberOfCards) throws EmptyPileOfCardsException;

    /**
     * creates a new discard pile for the game
     *
     * @return the new deck
     */
    PileOfCards getNewDiscardPile();
    /**
     * Gets the top card from the discard pile.
     *
     * @param discardPile the discard pile
     * @return the top card of the discard pile
     */
    Card getTopCard(PileOfCards discardPile);

    /**
     * Adds a card to the pile of cards
     *
     * @param pileOfCards the pile of cards
     * @param card the card to be added
     * @return updated pile of cards
     */
    PileOfCards addOneCard(PileOfCards pileOfCards, Card card);

    /**
     * Adds list of cards to the pile of cards
     *
     * @param pileOfCards the pile of cards
     * @param cards the cards to be added
     * @return updated pile of cards
     */
    PileOfCards addSeveralCards(PileOfCards pileOfCards, List<Card> cards);

    /**
     *
     * @param pile pile of cards that needs to be sorted
     * @return sorted pile of cards
     */
    PileOfCards sortCards(PileOfCards pile);

    /**
     * this method is used for player's hand
     * @return new pile of cards with id assigned
     */
    PileOfCards getNewPileOfCards();

    /**
     * this method is used for a player to play one card, this card must be removed from player's hand
     * @param pile hand
     * @param cardToBeDrawn card
     * @return new hand
     */
    PileOfCards drawASpecificCard(PileOfCards pile, Card cardToBeDrawn) throws CardNotFoundException;

    /**
     * save entity
     * @param pile
     * @return pile of cards
     */
    PileOfCards savePileOfCards(PileOfCards pile);

    /**
     * create new empty pile of cards
     * @return empty pile of cards with id
     */
    PileOfCards newEmptyPileOfCards();

    /**
     * get pile of cards by id
     * @param id
     * @return pile of cards
     */
    PileOfCards getPileOfCardsById(Long id);
}
