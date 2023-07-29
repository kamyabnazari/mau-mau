package htw.berlin.de.material_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Comparator;

@Entity
@Table(name = "Cards")
public class Card implements Comparable<Card> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "suit")
    @Enumerated(EnumType.STRING)
    private Suits suit;
    @Column(name = "rank")
    @Enumerated(EnumType.STRING)
    private Ranks rank;
    @Column(name = "orderInPile")
    private int order;
    @ManyToOne
    @JoinColumn(name ="pile_id")
    @JsonIgnore
    private PileOfCards owner;

    public Card(Long id ,Suits suit, Ranks rank) {
        this.id = id;
        this.suit = suit;
        this.rank = rank;
    }

    public Card(Suits suit, Ranks rank){
        this.suit = suit;
        this.rank = rank;
    }
    public Card() {
    }

    @Override
    public int compareTo(Card o) {
        return Comparator.comparing(Card::getRank)
                .thenComparing(Card::getSuit).compare(this, o);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suits getSuit() {
        return suit;
    }

    public void setSuit(Suits suit) {
        this.suit = suit;
    }

    public Ranks getRank() {
        return rank;
    }

    public void setRank(Ranks rank) {
        this.rank = rank;
    }

    public PileOfCards getOwner() {
        return owner;
    }

    public void setOwner(PileOfCards owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.getRank().toString() + "-" + this.getSuit().toString();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
