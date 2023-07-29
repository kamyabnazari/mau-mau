package htw.berlin.de.player_manager.serviceImpl;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.material_system.export.PileOfCardsService;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import htw.berlin.de.player_manager.entity.Player;
import htw.berlin.de.player_manager.repository.PlayerRepository;
import htw.berlin.de.player_manager.export.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the player service.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PileOfCardsService pileOfCardsService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PileOfCardsService pileOfCardsService) {
        this.playerRepository = playerRepository;
        this.pileOfCardsService = pileOfCardsService;
    }

    @Override
    public Player createPlayer(String name) {
        Player newPlayer = new Player(name);
        PileOfCards hand = pileOfCardsService.newEmptyPileOfCards();
        newPlayer.setHand(hand);
        return playerRepository.save(newPlayer);
    }

    @Override
    public Player getPlayerById(Long playerId) throws PlayerNotFoundException {
        Optional<Player> player = playerRepository.findById(playerId);
        if(player.isPresent()) return player.get();
        else throw new PlayerNotFoundException("Player with this id: " + playerId + " cannot be found");
    }

    @Override
    public Player replacePlayer(Player player) throws PlayerNotFoundException {
        Player oldPlayer = getPlayerById(player.getId());
        playerRepository.delete(oldPlayer);
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    @Override
    public Card addOneCardToHand(Player player, Card card) {
        PileOfCards hand = player.getHand();
        player.setHand(pileOfCardsService.addOneCard(hand, card));
        playerRepository.save(player);
        return card;
    }

    @Override
    public Player addManyCardsToHand(Player player, List<Card> cards) {
        PileOfCards hand = player.getHand();
        player.setHand(pileOfCardsService.addSeveralCards(hand, cards));
        playerRepository.save(player);
        return player;
    }

    @Override
    public Player playOneCard(Player player, Card card) throws CardNotFoundException {
        PileOfCards playerHand = player.getHand();
        PileOfCards newHand = pileOfCardsService.drawASpecificCard(playerHand, card);
        player.setHand(newHand);
        playerRepository.save(player);
        return player;
    }

    @Override
    public Player makeMauMauCall(Player player) {
        player.setMauMauCall(true);
        playerRepository.save(player);
        return player;
    }
}
