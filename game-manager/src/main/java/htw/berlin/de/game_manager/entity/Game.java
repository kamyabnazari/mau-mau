package htw.berlin.de.game_manager.entity;

import java.util.ArrayList;
import java.util.List;

import htw.berlin.de.game_manager.entity.Directions;
import htw.berlin.de.material_system.entity.PileOfCards;
import htw.berlin.de.material_system.entity.Suits;
import htw.berlin.de.player_manager.entity.Player;
import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private Directions direction;
    @Enumerated(EnumType.STRING)
    private Suits suit;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deck_id", referencedColumnName = "id")
    private PileOfCards deck;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discardPile_id", referencedColumnName = "id")
    private PileOfCards discardPile;
    @OneToMany(targetEntity = Player.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Player> playerList;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_player_id")
    private Player currentPlayer;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_player_id")
    private Player nextPlayer;
    private boolean accumulativeDrawn;
    private boolean isRunning;

    public Game(Long id, Directions direction, Suits suit, PileOfCards deck, PileOfCards discardPile) {
        this.id = id;
        this.direction = direction;
        this.suit = suit;
        this.deck = deck;
        this.discardPile = discardPile;
        // game must exist first
        this.playerList = new ArrayList<>();
        this.isRunning = true;
        this.accumulativeDrawn = false;
    }

    public Game() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public Suits getSuit() {
        return suit;
    }

    public void setSuit(Suits suit) {
        this.suit = suit;
    }

    public PileOfCards getDeck() {
        return deck;
    }

    public void setDeck (PileOfCards deck) {
        this.deck = deck;
    }

    public PileOfCards getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(PileOfCards discardPile) {
        this.discardPile = discardPile;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public boolean isAccumulativeDrawn() {
        return accumulativeDrawn;
    }

    public void setAccumulativeDrawn(boolean accumulativeDrawn) {
        this.accumulativeDrawn = accumulativeDrawn;
    }
}
