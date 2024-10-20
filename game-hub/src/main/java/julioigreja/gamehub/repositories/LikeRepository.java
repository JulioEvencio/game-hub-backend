package julioigreja.gamehub.repositories;

import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.LikeEntity;
import julioigreja.gamehub.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

    Optional<LikeEntity> findByUserAndGame(UserEntity user, GameEntity game);

}
