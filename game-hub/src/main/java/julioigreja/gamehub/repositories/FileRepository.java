package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    Optional<FileEntity> findByGame_Slug(String gameSlug);

}
