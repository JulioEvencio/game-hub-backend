package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    Optional<GameEntity> findByName(String name);

    Optional<GameEntity> findBySlug(String slug);

}
