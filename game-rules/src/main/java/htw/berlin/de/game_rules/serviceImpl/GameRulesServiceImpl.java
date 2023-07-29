package htw.berlin.de.game_rules.serviceImpl;

import htw.berlin.de.game_rules.export.GameRulesService;
import htw.berlin.de.game_rules.export.GameTechnicalErrorException;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.game_rules.serviceImpl.Actions;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.PileOfCardsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * RulesServiceImpl class for the game rules.
 */
@Service
public class GameRulesServiceImpl implements GameRulesService {

    // TODO: How should this work?

    private HashMap<Actions, Ranks> activeRules ;
    private PileOfCardsService pileOfCardsService;
    private final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    public GameRulesServiceImpl(PileOfCardsService pileOfCardsService) {
        this.pileOfCardsService = pileOfCardsService;
        this.activeRules = new HashMap<>();
        activeRules.put(Actions.DRAW_ACCUMULATIVE, Ranks.SEVEN);
        activeRules.put(Actions.CHANGE_SUIT, Ranks.JACK);
    }

    public GameRulesServiceImpl() {

    }


    @Override
    public int getTopAccumulativeCardsPlayed(PileOfCards discardPile){
        List<Card> cardsInDiscardPile = discardPile.getListOfCard();
        int sum = 0;
        for(int i = cardsInDiscardPile.size() - 1; i>=0;i--){
            if(cardsInDiscardPile.get(i).getRank()==activeRules.get(Actions.DRAW_ACCUMULATIVE)){
                sum += 1;
            }
            else {
                break;
            }
        }
        return sum;
    }

    @Override
    public boolean canPlay(Card card, Card topCardOfDiscardPile, Suits currentSuitOfTheGame, boolean accumulativeDrawn) {
        if(topCardOfDiscardPile.getRank() != activeRules.get(Actions.DRAW_ACCUMULATIVE)){
            if(card.getRank() == activeRules.get(Actions.CHANGE_SUIT)) return true;
            if(topCardOfDiscardPile.getRank() == activeRules.get(Actions.CHANGE_SUIT)) {
                return card.getSuit() == currentSuitOfTheGame;
            }
            return card.getSuit() == topCardOfDiscardPile.getSuit() || card.getRank() == topCardOfDiscardPile.getRank();
        }
        else {
            if(accumulativeDrawn) return card.getSuit() == topCardOfDiscardPile.getSuit();
            else return false;
        }
    }

    /**
     * Check if card has a game action.
     *
     * @param card the card
     * @return the game rule for the card
     */
    public Optional<Ranks> checkIfCardHasGameRule(Card card) throws GameTechnicalErrorException {
        try {
            if (activeRules.containsKey(card.getRank())) {
                LOGGER.info("card was found to have a rank");
                return Optional.of(activeRules.get(card.getRank()));
            }
            LOGGER.info("no rank found");
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("something went wrong getting the rank and comparing it to the rules");
            throw new GameTechnicalErrorException(String.format("Given Card is empty: %s", e.getMessage()));
        }
    }

    @Override
    public HashMap<Actions, Ranks> getActiveRules(){
        return activeRules;
    }

    @Override
    public HashMap<Actions, Ranks> setActiveRules(HashMap<Actions, Ranks> activeRules) {
        return this.activeRules;
    }
}
