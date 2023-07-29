package htw.berlin.de.game_manager.repository;

import htw.berlin.de.game_manager.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
