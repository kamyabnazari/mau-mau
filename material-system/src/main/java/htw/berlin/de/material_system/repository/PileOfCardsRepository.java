package htw.berlin.de.material_system.repository;

import htw.berlin.de.material_system.entity.PileOfCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PileOfCardsRepository extends JpaRepository<PileOfCards, Long> {
}

