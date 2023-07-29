package htw.berlin.de.game_manager.export;

import htw.berlin.de.game_manager.entity.Directions;
import htw.berlin.de.game_manager.entity.Game;
import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.game_rules.serviceImpl.Actions;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.player_manager.entity.Player;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface GameService {

    /** 0
     * this method instantiates a new Game object and assign an pre-generated id to
     * it
     *
     * @param listOfPlayers list of players that participate in this game
     * @return a newly instantiated game
     */
    Game instantiateGame(List<Player> listOfPlayers);

    /** 0
     * Checks if any player has won the game (i.e., if they have no more cards in
     * their hand) and ends the game.
     *
     * @param game the game to be ended
     * @param player the player which has no cards anymore.
     */
    void checkEndGame(Game game, Player player);

    /** 1
     * Plays one turn for the specified player. The player must choose a card from
     * their hand that matches either the current suit or the current value. If the
     * player cannot play a card, they must draw a card from the deck. If the deck
     * is empty, the deck must be refilled
     *
     * @param gameId id of the game instance that is currently being played
     * @param playerId id of the player who is in turn to play
     * @param action action the player was forced to do (in case of) or chose to do
     * @param cardToBePlayedId id of card to be played
     * @param suit in case of a player play a card that allow user to choose the suit of the game
     * @param mauMauCalled this mau mau called indicates if the user has called it in their turn before playing their
     *                     second last card (they make mau mau call any turn without being fined)
     * @return Game instance with most current state
     */
    Game playTurn(Long gameId, Long playerId, Actions action, Long cardToBePlayedId, Suits suit, boolean mauMauCalled) throws CardNotFoundException, PlayerNotFoundException, GameNotFoundException, EmptyPileOfCardsException, InvalidRuleException;


    /** 0
     * use this method to change suit of the game and save game instance
     * @param game game instance that is currently being played
     * @param suit suit
     */
    //should be private
    void changeSuit(Game game, Suits suit);


    /** 0
     * play a card
     *
     * @param game the game instance that is currently being played
     * @param player the player who plays this card
     * @param discardPile the discard pile
     * @param card        the card to be put on top of the discard pile
     */
    void playCard(Game game, Player player, PileOfCards discardPile, Card card) throws CardNotFoundException, InvalidRuleException;

    /** 1
     *
     * @param gameId id of the game
     * @return game instance with this id
     */
    Game getGameById(Long gameId);

    /** 1
     *
     * @param game game instance containing new data to be updated
     * @return updated game instance
     */
    Game updateGame(Game game);

    /** 1
     *
     * @param gameId id of the game to be deleted
     */
    void deleteGame(Long gameId);

    /** 0
     *
     * @param game game instance that is currently being played
     * @return the next player's index in the player list
     */
    int getNextPlayersIndex(Game game);

    /** 0
     * this method is used to deal cards to players in the beginning of the game
     * @param deck
     * @param playerList
     */
    void dealCard(PileOfCards deck, List<Player> playerList) throws EmptyPileOfCardsException;

    /** 0
     * create a new user with name
     * @param playerName player's name
     * @return player instance
     */
    Player createPlayer(String playerName);

    /** 1
     * this method is used when for setup a game.
     *
     * @param playerName name of the player
     * @throws Exception
     */
    Game startGame(String playerName);

    /**
     *
     * @param gameId id of the game instance that is currently being played
     * @param playerIndex player's index in player's list in game instance.
     *                    It's player's turn to play. Before playing, UI should call this to check all the cards that can be played
     *                    so the user can choose one of them
     * @return list of cards that are playable in this turn
     */
    List<Card> getPlayableCardsFromHand(Long gameId, int playerIndex) throws GameNotFoundException;

    /**
     * get player by id
     * @param playerId player id
     * @return player
     */
    Player getPlayerById(Long playerId) throws PlayerNotFoundException;

    /**
     * get pile of cards by id
     * @param id pile of cards id
     * @return pile of card
     */
    PileOfCards getPileOfCards(Long id);

    /**
     * get all games
     * @return list of all games
     */
    List<Game> getAllGames();

    /**
     * check if a card can be played by a certain user
     * @param gameId game id
     * @param userId id of the user
     * @param cardId id of the card
     * @return
     */
    boolean canPlayThisCard(Long gameId, Long userId, Long cardId);
}
