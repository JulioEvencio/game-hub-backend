package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.ScreenshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScreenshotRepository extends JpaRepository<ScreenshotEntity, UUID> {

    Optional<ScreenshotEntity> findByGame_SlugAndId(String gameSlug, UUID id);

}
