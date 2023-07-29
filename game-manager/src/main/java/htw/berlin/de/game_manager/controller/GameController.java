package htw.berlin.de.game_manager.controller;

import htw.berlin.de.game_manager.entity.Game;
import htw.berlin.de.game_manager.export.GameNotFoundException;
import htw.berlin.de.game_manager.export.GameService;
import htw.berlin.de.game_rules.export.InvalidRuleException;
import htw.berlin.de.game_rules.serviceImpl.Actions;
import htw.berlin.de.material_system.entity.Card;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.player_manager.entity.Player;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("game-rest")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/playTurn")
    public ResponseEntity<Game> playTurn(@RequestParam Long gameId, @RequestParam Long userId,
            @RequestParam Actions action, @RequestParam Long cardToBePlayedId, @RequestParam Suits suit,
            @RequestParam boolean mauMauCalled) {
        Game game = null;
        game = gameService.playTurn(gameId, userId, action, cardToBePlayedId, suit, mauMauCalled);
        return ResponseEntity.ok(game);
    }

    @PostMapping(value = "/startGame", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> startGame(@RequestBody String name) {
        Game game = gameService.startGame(name);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{id}/game")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping(value = "/{id}/saveGame", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        // make sure id from path variable and game instance match
        game.setId(id);
        gameService.updateGame(game);
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/{id}/deleteGame")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    @GetMapping("/get-playable-cards")
    public ResponseEntity<List<Card>> getPlayableCardsFromHand(@RequestParam Long gameId,
            @RequestParam int playerIndex) {
        try {
            List<Card> cardList = gameService.getPlayableCardsFromHand(gameId, playerIndex);
            return ResponseEntity.ok(cardList);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{id}/player")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getPlayerById(id));
    }

    @GetMapping("/{id}/pileOfCards")
    public ResponseEntity<PileOfCards> getPileOfCards(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getPileOfCards(id));
    }

    @GetMapping("/allGames")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.of(java.util.Optional.ofNullable(gameService.getAllGames()));
    }

    // @GetMapping("/playable")
    // public ResponseEntity<Boolean> canPlayThisCard(@PathVariable Long gameId,
    // @PathVariable Long userId, @PathVariable Long cardId){
    // return null;
    // }

}
