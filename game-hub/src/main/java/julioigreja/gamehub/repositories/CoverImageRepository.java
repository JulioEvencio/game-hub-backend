package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.CoverImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoverImageRepository extends JpaRepository<CoverImageEntity, UUID> {

    Optional<CoverImageEntity> findByGame_Slug(String gameSlug);

}
