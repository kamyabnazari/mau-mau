package htw.berlin.de.material_system.serviceImpl;

import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.repository.PileOfCardsRepository;
import htw.berlin.de.material_system.export.PileOfCardsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class implements the DeckService interface.
 */
@Service
public class PileOfCardsServiceImpl implements PileOfCardsService {

    PileOfCardsRepository pileOfCardsRepository;
    CardService cardService;
    private final Log LOGGER = LogFactory.getLog(getClass());
    @Autowired
    public PileOfCardsServiceImpl(PileOfCardsRepository pileOfCardsRepository, CardService cardService){
        this.pileOfCardsRepository = pileOfCardsRepository;
        this.cardService = cardService;
    }
    @Override
    public PileOfCards getNewDeck() throws CardNotFoundException {
        PileOfCards newDeck = new PileOfCards();
        PileOfCards newDeckWithId = pileOfCardsRepository.save(newDeck);
        List<Card> listOfCards = new ArrayList<>();
        for(int i = 1; i <= 52; i++){
                Card newCard = cardService.getCardById(Long.valueOf(i));
                listOfCards.add(newCard);
            }
        //shuffle after initiating
        Collections.shuffle(listOfCards);
        newDeckWithId.setListOfCard(listOfCards);
        cardService.saveCardsInPileOfCard(newDeckWithId, listOfCards);
        pileOfCardsRepository.save(newDeckWithId);
        return newDeckWithId;
    }

    @Override
    public PileOfCards shuffle(PileOfCards deck) {
        List<Card> listOfCards = deck.getListOfCard();
        Collections.shuffle(listOfCards);
        deck.setListOfCard(listOfCards);
        pileOfCardsRepository.save(deck);
        return deck;
    }

    public void refillDeck(PileOfCards deck, PileOfCards discardPile) {
        if(deck.getListOfCard().size() == 0) {
            List<Card> discardList = discardPile.getListOfCard();
            Card cardLeftInDiscardPile = discardList.get(discardList.size()-1);
            List<Card> cardLeftInDiscardPileList = new ArrayList<>();
            cardLeftInDiscardPileList.add(cardLeftInDiscardPile);
            List<Card> cardsToBePutToDeck = new ArrayList<>(discardList.subList(0, discardList.size()-1));
            Collections.shuffle(cardsToBePutToDeck);
            deck.setListOfCard(cardsToBePutToDeck);
            cardService.saveCardsInPileOfCard(deck, cardsToBePutToDeck);
            discardPile.setListOfCard(cardLeftInDiscardPileList);
            cardService.saveCardsInPileOfCard(discardPile, cardLeftInDiscardPileList);
            pileOfCardsRepository.save(deck);
            pileOfCardsRepository.save(discardPile);
        }
    }

    public List<Card> drawCards(PileOfCards deck, int numberOfCards) throws EmptyPileOfCardsException {
        //draw card from the index size - 1
        List<Card> result = new ArrayList<>();
        if(deck.getListOfCard().isEmpty()){
            throw new EmptyPileOfCardsException("deck is empty");
        }
        else {
            int sum = numberOfCards;
            ArrayList<Card> listCard = new ArrayList<>(deck.getListOfCard());
            while(sum!=0){
                sum--;
                Card removedCard = listCard.remove(listCard.size()-1);
                result.add(removedCard);
            }
            deck.setListOfCard(listCard);
            cardService.saveCardsInPileOfCard(deck, listCard);
        }
        //deck has fewer cards now
        pileOfCardsRepository.save(deck);
        return result;
    }

    @Override
    public PileOfCards getNewDiscardPile() {
        PileOfCards discardPile = new PileOfCards();
        discardPile.setListOfCard(new ArrayList<>());
        return pileOfCardsRepository.save(discardPile);
    }


    @Override
    public Card getTopCard(PileOfCards discardPile) {

        if(discardPile.getListOfCard() != null && !discardPile.getListOfCard().isEmpty())
        {
            return discardPile.getListOfCard().get(discardPile.getListOfCard().size() - 1);
        }
        else {
            LOGGER.warn("The discard pile is empty, try fill the discard pile");
            return null;
        }
    }


    @Override
    public PileOfCards getPileOfCardsById(Long id) {
        Optional<PileOfCards> pile = pileOfCardsRepository.findById(id);
        if(pile.isPresent()){
            return pile.get();
        }
        else return null;
    }

    @Override
    public PileOfCards addOneCard(PileOfCards pileOfCards, Card card) {
        List<Card> cardList = pileOfCards.getListOfCard();
        cardList.add(card);
        pileOfCards.setListOfCard(cardList);
        cardService.saveCardsInPileOfCard(pileOfCards, cardList);
        pileOfCardsRepository.save(pileOfCards);
        return pileOfCards;
    }

    @Override
    public PileOfCards addSeveralCards(PileOfCards pileOfCards, List<Card> cards) {
        List<Card> cardList = pileOfCards.getListOfCard();
        cardList.addAll(cards);
        pileOfCards.setListOfCard(cardList);
        cardService.saveCardsInPileOfCard(pileOfCards, cardList);
        pileOfCardsRepository.save(pileOfCards);
        return pileOfCards;
    }

    @Override
    public PileOfCards sortCards(PileOfCards pile) {
        List<Card> sortedCards = pile.getListOfCard().stream().sorted(Card::compareTo).toList();
        pile.setListOfCard(sortedCards);
        pileOfCardsRepository.save(pile);
        return pile;
    }

    @Override
    public PileOfCards getNewPileOfCards() {
        PileOfCards hand = new PileOfCards();
        hand.setListOfCard(new ArrayList<>());
        pileOfCardsRepository.save(hand);
        return hand;
    }

    @Override
    public PileOfCards drawASpecificCard(PileOfCards pile, Card cardToBeDrawn) throws CardNotFoundException {
        List<Card> listOfCards = pile.getListOfCard();
        if(listOfCards.indexOf(cardToBeDrawn) == -1){
            throw new CardNotFoundException("Card that should be played is not in this pile, this action cannot be done");
        }
        else {
            listOfCards.remove(cardToBeDrawn);
            pile.setListOfCard(listOfCards);
            cardService.saveCardsInPileOfCard(pile, listOfCards);
        }
        pileOfCardsRepository.save(pile);
        return pile;
    }

    @Override
    public PileOfCards savePileOfCards(PileOfCards pile){
        return pileOfCardsRepository.save(pile);
    }

    @Override
    public PileOfCards newEmptyPileOfCards(){
        PileOfCards emptyPile = new PileOfCards();
        List<Card> emptyCardList = new ArrayList<>();
        emptyPile.setListOfCard(emptyCardList);
        return pileOfCardsRepository.save(emptyPile);
    }
}
