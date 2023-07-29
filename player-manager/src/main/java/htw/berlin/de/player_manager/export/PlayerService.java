package htw.berlin.de.player_manager.export;

import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.player_manager.entity.Player;

import java.util.List;


/**
 * The interface for methods involving creating or changing a Player.
 */
public interface PlayerService {
    /**
     * Creates a player with the specified name.
     *
     * @param name the name of the player
     * @return the created player object
     */
    Player createPlayer(String name) ;

    /**
     * Retrieves a player by their ID.
     *
     * @param playerId the ID of the player
     * @return the player object
     * @throws PlayerNotFoundException when player cannot be found
     */
    Player getPlayerById(Long playerId) throws PlayerNotFoundException;

    /**
     * Updates a player.
     *
     * @param player the player to be updated
     * @return the updated player object
     * @throws PlayerNotFoundException when player to be replaced cannot be found
     */
    Player replacePlayer(Player player) throws PlayerNotFoundException;

    /**
     * Deletes a player by their ID.
     *
     * @param player player to be deleted
     */
    void deletePlayer(Player player) ;


    /**
     * @param player player who has just drawn a card
     * @param card card to be added to hand after drawing
     * @return card
     */
    Card addOneCardToHand(Player player, Card card);

    /**
     *
     * @param player player who is forced to draw these cards
     * @param cards list of cards
     * @return player
     */
    Player addManyCardsToHand(Player player, List<Card> cards);

    /**
     * this method is used for when a player wants to play a card
     * @param player player
     * @param card card to be played from player's hand
     * @return player with new hand
     */
    Player playOneCard(Player player, Card card) throws CardNotFoundException;

    Player makeMauMauCall(Player player);
}