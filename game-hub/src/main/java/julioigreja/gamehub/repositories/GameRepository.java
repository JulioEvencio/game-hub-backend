package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    @NonNull
    Page<GameEntity> findAll(@NonNull Pageable pageable);

    Optional<GameEntity> findByName(String name);

    Optional<GameEntity> findBySlug(String slug);

}
