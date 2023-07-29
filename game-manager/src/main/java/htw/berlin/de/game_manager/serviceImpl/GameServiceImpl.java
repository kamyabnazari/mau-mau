package htw.berlin.de.game_manager.serviceImpl;

import htw.berlin.de.game_manager.entity.Directions;
import htw.berlin.de.game_manager.entity.Game;
import htw.berlin.de.game_manager.export.GameNotFoundException;
import htw.berlin.de.game_manager.repository.GameRepository;
import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.game_rules.serviceImpl.Actions;
import htw.berlin.de.game_rules.export.GameRulesService;
import htw.berlin.de.material_system.entity.Ranks;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.CardService;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.PileOfCardsService;
import htw.berlin.de.player_manager.entity.Player;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import htw.berlin.de.player_manager.export.PlayerService;
import htw.berlin.de.player_manager.repository.PlayerRepository;
import htw.berlin.de.game_manager.export.GameService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PileOfCardsService pileOfCardsService;
    private final PlayerService playerService;
    private final GameRulesService gameRulesService;
    private final CardService cardService;
    private final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PileOfCardsService pileOfCardsService,
                           PlayerService playerService, GameRulesService gameRulesService, CardService cardService) {
        this.gameRepository = gameRepository;
        this.pileOfCardsService = pileOfCardsService;
        this.playerService = playerService;
        this.gameRulesService = gameRulesService;
        this.cardService = cardService;
    }

    @Override
    public Game instantiateGame(List<Player> listOfPlayers) {
        Game newGame = new Game();
        try {
            //instantiate new deck and discard pile
            PileOfCards deck = pileOfCardsService.getNewDeck();
            PileOfCards discardPile = pileOfCardsService.getNewDiscardPile();
            //draw first card to use as a top card of discard pile
            List<Card> oneCardDrawn = pileOfCardsService.drawCards(deck, 1);
            Card oneCard = oneCardDrawn.get(0);
            pileOfCardsService.addOneCard(discardPile,oneCard);
            //deal cards to players
            dealCard(deck, listOfPlayers);
            //set all the initial state of the game
            newGame.setPlayerList(listOfPlayers);
            newGame.setDirection(Directions.CLOCKWISE);
            newGame.setRunning(true);
            newGame.setCurrentPlayer(listOfPlayers.get(0));
            newGame.setNextPlayer(listOfPlayers.get(1));
            //save the deck and discard pile into game
            newGame.setDeck(deck);
            newGame.setDiscardPile(discardPile);
            //set suit of the game
            newGame.setSuit(oneCard.getSuit());
            gameRepository.save(newGame);
        } catch (CardNotFoundException | EmptyPileOfCardsException e) {
            e.printStackTrace();
        }
        return newGame;
    }

    @Override
    public void checkEndGame(Game game, Player player) {
        if(player.isMauMauCall() && player.getHand().getListOfCard().isEmpty()){
            game.setRunning(false);
            LOGGER.info("game is over, player with id:" + player.getId() + " won");
            gameRepository.save(game);
        }
    }

    @Override
    public Game playTurn(Long gameId, Long playerId, Actions action, Long cardToBePlayedId, Suits suit, boolean mauMauCalled) throws InvalidRuleException{
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        try{
            Player player = playerService.getPlayerById(playerId);
            Card cardToBePlayed = cardService.getCardById(cardToBePlayedId);
            if(gameOptional.isPresent()){
                Game game = gameOptional.get();
                //for this version player list has at index 0 the user, at index 1 the virtual player, this order won't be changed
                Player virtualPlayer = game.getPlayerList().get(1);
                // player's turn
                playTurn(game, player, action, cardToBePlayed, suit, mauMauCalled);
                // virtual player's turn
                playTurnVirtualPlayer(gameId, virtualPlayer);
                return game;
            }
            else throw new GameNotFoundException("Cannot find game with id: " + gameId);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        } catch (CardNotFoundException e) {
            e.printStackTrace();
        } catch (GameNotFoundException gameNotFoundException) {
            gameNotFoundException.printStackTrace();
        } catch (EmptyPileOfCardsException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playTurn(Game game, Player player, Actions action, Card cardToBePlayed, Suits suit, boolean mauMauCalled)
            throws CardNotFoundException, PlayerNotFoundException, GameNotFoundException, EmptyPileOfCardsException, InvalidRuleException{
        switch(action){
            case DRAW -> {
                try {
                    int numberOfCardsToBeDrawn = getNumberOfCardsToBeDrawn(game);
                    LOGGER.info("Player with id "+ player.getId() +" has to draw " + numberOfCardsToBeDrawn + " cards");
                    // drawing action chain
                    Player playerWithNewHand = drawCardsActionChain(game, player, numberOfCardsToBeDrawn);
                    // set list of player for game player with new hand
                    saveGameWithNewPlayersHand(game, playerWithNewHand);
                    Player nextPlayer = game.getPlayerList().get(getNextPlayersIndex(game));
                    game.setCurrentPlayer(nextPlayer);
                    gameRepository.save(game);
                    break;
                } catch (EmptyPileOfCardsException e ) {
                    e.printStackTrace();
                } catch (InvalidRuleException e) {
                    e.printStackTrace();
                }
            }
            case PLAY -> {
                if(player.getHand().getListOfCard().contains(cardToBePlayed)){
                    playCard(game, player, game.getDiscardPile(), cardToBePlayed);
                } else {
                    throw new CardNotFoundException("card with id:" + cardToBePlayed.toString() + " is not in player's hand");
                }

                if(cardToBePlayed.getRank() == gameRulesService.getActiveRules().get(Actions.CHANGE_SUIT)){
                    changeSuit(game, suit);
                }

                // accept mau mau call from user after they play their 2nd last card
                if(player.getHand().getListOfCard().size() == 1 && mauMauCalled) {
                    player.setMauMauCall(true);
                    player = playerService.makeMauMauCall(player);
                    LOGGER.info("user made mau mau call");
                } else {
                    if(player.getHand().getListOfCard().size() == 1 && !mauMauCalled){
                        //fine 2 cards
                        //drawing action chain
                        Player playerWithNewHand = drawCardsActionChain(game, player, 2);
                        //set list of player for game player with new hand
                        saveGameWithNewPlayersHand(game, playerWithNewHand);
                        LOGGER.info("user did not make mau mau call on time, must draw 2 cards");
                    }
                }
                Player nextPlayer = game.getPlayerList().get(getNextPlayersIndex(game));
                game.setCurrentPlayer(nextPlayer);
                gameRepository.save(game);
                break;
            }
        }
        checkEndGame(game, player);
    }

    private int getNumberOfCardsToBeDrawn(Game game) throws InvalidRuleException {
        int accumulativeCards = gameRulesService.getTopAccumulativeCardsPlayed(game.getDiscardPile());
        if(accumulativeCards == 0) {
            game.setAccumulativeDrawn(false);
            gameRepository.save(game);
            return 1;
        }
        else {
            if(game.isAccumulativeDrawn()){
                LOGGER.info("accumulative drawn? :" + game.isAccumulativeDrawn());
                return 1;
            }
            else {
                LOGGER.info("accumulative drawn? :" + game.isAccumulativeDrawn());
                game.setAccumulativeDrawn(true);
                gameRepository.save(game);
                return 2;
            }
        }
    }

    private Player drawCardsActionChain(Game game,Player player, int numberOfCardsToBeDrawn) throws EmptyPileOfCardsException {
        //have to check and refill deck if necessary before drawing and execute the dra
        if(game.getDeck().getListOfCard().size() <= numberOfCardsToBeDrawn){
            PileOfCards deck = game.getDeck();
            PileOfCards discardPile = game.getDiscardPile();
            pileOfCardsService.refillDeck(deck, discardPile);
            LOGGER.info("refill deck from discard pile");
        }
        //execute the drawing action
        List<Card> cards = pileOfCardsService.drawCards(game.getDeck(), numberOfCardsToBeDrawn);
        for(Card card : cards){
            LOGGER.info("drew card: " + card.toString());
        }
        Player playerWithNewHand = playerService.addManyCardsToHand(player, cards);
        return playerWithNewHand;
    }

    private Game saveGameWithNewPlayersHand(Game game, Player player){
        List<Player> currentPlayerList = game.getPlayerList();
        List<Long> currentPlayerIdList = game.getPlayerList().stream().map(x -> x.getId()).toList();
        int indexOfPlayer = currentPlayerIdList.indexOf(player.getId());
        currentPlayerList.set(indexOfPlayer, player);
        game.setPlayerList(currentPlayerList);
        gameRepository.save(game);
        return game;
    }


    @Override
    public void changeSuit(Game game, Suits suit) {
        game.setSuit(suit);
        gameRepository.save(game);
    }

    @Override
    public void playCard(Game game, Player player, PileOfCards discardPile, Card card) throws CardNotFoundException, InvalidRuleException {
        boolean canPlay = (gameRulesService.canPlay(card, pileOfCardsService.getTopCard(game.getDiscardPile()), game.getSuit(), game.isAccumulativeDrawn()) ? true : false);
        if(canPlay) {
            //TODO remove a card from player's hand and update player and update list of player in game
            Player playerWithNewHand = playerService.playOneCard(player, card);
            saveGameWithNewPlayersHand(game, playerWithNewHand);
            PileOfCards newDiscardPile = pileOfCardsService.addOneCard(game.getDiscardPile(), card);
            game.setSuit(card.getSuit());
            gameRepository.save(game);
        }
        else {
            LOGGER.info("player cannot play this card");
            throw new InvalidRuleException("player cannot play card with id: " + card.getId());
        }
    }

    @Override
    public Game getGameById(Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()) return game.get();
        else return null;
    }

    @Override
    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    @Override
    public int getNextPlayersIndex(Game game) {
        int indexOfCurrentPlayer = game.getPlayerList().indexOf(game.getCurrentPlayer());
        int playerListSize = game.getPlayerList().size();
        return indexOfCurrentPlayer == playerListSize - 1 ? 0 : indexOfCurrentPlayer + 1;
    }

    @Override
    public void dealCard(PileOfCards deck, List<Player> playerList) throws EmptyPileOfCardsException {
        for(Player player : playerList){
            List<Card> hand = pileOfCardsService.drawCards(deck, 7);
            playerService.addManyCardsToHand(player, hand);
        }
    }

    @Override
    public Player createPlayer(String playerName) {
        return playerService.createPlayer(playerName);
    }

    @Override
    public Game startGame(String playerName)  {
        Player player = createPlayer(playerName);
        Player virtualPlayer = createPlayer("Virtual Player");
        Game game = instantiateGame(List.of(player, virtualPlayer));
        return game;
    }

    @Override
    public List<Card> getPlayableCardsFromHand(Long gameId, int playerIndex) throws GameNotFoundException{
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        if(gameOpt.isPresent()){
            Game game = gameOpt.get();
            Player player = game.getPlayerList().get(playerIndex);
            List<Card> hand = player.getHand().getListOfCard();
            List<Card> playableCards = new ArrayList<>();
            for(Card card : hand){
                if(gameRulesService.canPlay(card, pileOfCardsService.getTopCard(game.getDiscardPile()), game.getSuit(), game.isAccumulativeDrawn())){
                    playableCards.add(card);
                }
            }
            return playableCards;
        }
      else throw new GameNotFoundException("game with id: " + gameId + " does not exist");
    }

    /**
     * this method makes decision for virtual player
     * @param gameId id of game instance
     * @param virtualPlayer virtual player that
     */
    private void playTurnVirtualPlayer(Long gameId, Player virtualPlayer) throws PlayerNotFoundException, CardNotFoundException, GameNotFoundException, EmptyPileOfCardsException, InvalidRuleException {
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        if(gameOpt.isPresent()){
            Game game = gameOpt.get();
            // check playable cards. Virtual player's index is always 1 for 2 players game. Index 0 is for user
            List<Card> playableList = getPlayableCardsFromHand(gameId, 1);
            // assign random suit
            Suits randomizeSuitChoice = randomizeSuitChoiceForVirtualPlayer();
            // always play if playable list isn't empty
            if(!playableList.isEmpty()){
                // mau mau will always be true because virtual player never forgets to make mau mau call, and premature mau mau call won't be fined
                playTurn(game, virtualPlayer, Actions.PLAY, playableList.get(0), randomizeSuitChoice, true);
                if(playableList.get(0).getRank() == Ranks.JACK){
                    LOGGER.info("virtual player chose suit: " + randomizeSuitChoice);
                }
                LOGGER.info("virtual player just played card: " + playableList.get(0).toString());
            }
            // if playable list is empty then draw
            else {
                //first card as place holder, won't be used
                Card dummyCard = cardService.getCardById(1L);
                playTurn(game, virtualPlayer, Actions.DRAW, dummyCard , randomizeSuitChoice, true);
                LOGGER.info("virtual player must draw cards");
            }
        }
    }

    private Suits randomizeSuitChoiceForVirtualPlayer(){
        Random rand = new Random();
        // Obtain a number between [0 - 3].
        int randomSuitAsInt = rand.nextInt(4);
        switch (randomSuitAsInt){
            case 0 : return Suits.HEART;
            case 1 : return Suits.DIAMOND;
            case 2 : return Suits.CLUB;
            case 3 : return Suits.SPADE;
            default: return null;
        }
    }

    @Override
    public Player getPlayerById(Long playerId) throws PlayerNotFoundException {
        return playerService.getPlayerById(playerId);
    }

    @Override
    public PileOfCards getPileOfCards(Long id) {
        return pileOfCardsService.getPileOfCardsById(id);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public boolean canPlayThisCard(Long gameId, Long userId, Long cardId) {
        //check if card is in user's hand
        return false;
    }
}
