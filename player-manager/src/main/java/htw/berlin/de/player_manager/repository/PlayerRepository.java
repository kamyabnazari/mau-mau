package htw.berlin.de.player_manager.repository;

import htw.berlin.de.player_manager.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PlayerRepository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
