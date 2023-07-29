package htw.berlin.de.material_system.serviceImpl;

import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.repository.CardRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Card service.
 */
@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    public CardServiceImpl(CardRepository repo) {
        this.cardRepository = repo;
    }

    @Override
    public Card createCard(Suits suit, Ranks rank) {
        Card card = new Card(suit, rank);
        return cardRepository.save(card);
    }

    @Override
    public String getCardAsString(Card card) {
        return card.getSuit() + " " + card.getRank();
    }

    @Override
    public Card getCardById(Long id) throws CardNotFoundException {
        Optional<Card> foundCard = cardRepository.findById(id);
        if(foundCard.isPresent()) return foundCard.get();
        else throw new CardNotFoundException("Card with id: " + id + " doesnt exist");
    }

    @Override
    public void updateCard(Long cardId, int order) throws CardNotFoundException {
        Card card = getCardById(cardId);
        card.setOrder(order);
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    @Override
    public PileOfCards saveCardsInPileOfCard(PileOfCards pile, List<Card> cardList) {
        // set the order of the cards in this pile according to the order of them in the card list
        for(Card card : cardList){
            // order starts from 1 not from 0
            int order = cardList.indexOf(card) + 1;
            card.setOrder(order);
            card.setOwner(pile);
            cardRepository.save(card);
        }
        return pile;
    }
}
