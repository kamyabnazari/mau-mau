package htw.berlin.de.material_system.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "pileOfCards")
public class PileOfCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "owner")
    @OrderBy("order asc")
    private List<Card> listOfCard;

    public PileOfCards(Long id, List<Card> listOfCard){
        this.id = id;
        this.listOfCard = listOfCard;
    }

    public PileOfCards(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Card> getListOfCard() {
        return listOfCard;
    }

    public void setListOfCard(List<Card> listOfCard) {
        this.listOfCard = listOfCard;
    }
}
