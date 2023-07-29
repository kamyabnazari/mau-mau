package htw.berlin.de.game_rules.export;

import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.game_rules.serviceImpl.Actions;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


public interface GameRulesService {

    /**
     * this method validate if the card is according to current game rules can be
     * played or not
     * 
     * @param card card that is being validated
     * @param topCardOfDiscardPile top card on the discard pile
     * @param currentSuitOfTheGame current suit of the game
     * @param accumulativeDrawn do decide if player can play according to suit or has to be fined
     * @return true if the card can be played and false otherwise
     */
    boolean canPlay(Card card, Card topCardOfDiscardPile, Suits currentSuitOfTheGame, boolean accumulativeDrawn) ;

    HashMap<Actions, Ranks> getActiveRules();
    /**
     * this method returns the number of cards that can be accumulated on the
     * discard pile
     * 
     * @param discardPile discard pile that is being validated
     * @return number of cards that can be accumulated on the discard pile
     */
    int getTopAccumulativeCardsPlayed(PileOfCards discardPile) throws InvalidRuleException;

    HashMap<Actions, Ranks> setActiveRules(HashMap<Actions, Ranks> activeRules);
    /**
     * Check if card has a game action.
     *
     * @param card the card
     * @return the game rule for the card
     */
    Optional<Ranks> checkIfCardHasGameRule(Card card) throws GameTechnicalErrorException;
}
