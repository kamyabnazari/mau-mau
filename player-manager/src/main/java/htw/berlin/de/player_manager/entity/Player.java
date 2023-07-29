package htw.berlin.de.player_manager.entity;

import htw.berlin.de.material_system.entity.PileOfCards;
import jakarta.persistence.*;

@Entity(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    private Long score;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hand_id", referencedColumnName = "id")
    private PileOfCards hand;
    private boolean mauMauCall;

    public Player(String name) {
        this.name = name;
        this.score = Long.valueOf(0);
        this.mauMauCall = false;
    }

    //for testing
    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
        this.score = Long.valueOf(0);
        this.hand = new PileOfCards();
        this.mauMauCall = false;
    }

    public Player() {

    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public PileOfCards getHand() {
        return hand;
    }

    public void setHand(PileOfCards pileOfCards) {
        this.hand = pileOfCards;
    }

    public boolean isMauMauCall() {
        return mauMauCall;
    }

    public void setMauMauCall(boolean mauMauCall) {
        this.mauMauCall = mauMauCall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
