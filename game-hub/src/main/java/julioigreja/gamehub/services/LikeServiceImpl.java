package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.like.LikeUpdateResponseDTO;
import julioigreja.gamehub.dto.controllers.like.LikeGetResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.LikeEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.repositories.GameRepository;
import julioigreja.gamehub.repositories.LikeRepository;
import julioigreja.gamehub.repositories.UserRepository;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final LikeRepository likeRepository;

    public LikeServiceImpl(UserRepository userRepository, GameRepository gameRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    @Override
    public LikeUpdateResponseDTO update(String gameSlug) {
        UserEntity user = ApiUtil.findUserLogged(userRepository);
        GameEntity game = gameRepository.findBySlug(gameSlug).orElseThrow(() -> new ApiNotFoundException("Game not found"));
        Optional<LikeEntity> optionalLike = likeRepository.findByUserAndGame(user, game);
        LikeEntity like = null;

        if (optionalLike.isPresent()) {
            like = optionalLike.get();

            like.setLike(!like.isLike());
        } else {
            like = new LikeEntity();

            like.setLike(true);
            like.setUser(user);
            like.setGame(game);
        }

        like = likeRepository.save(like);

        return new LikeUpdateResponseDTO(EntityMapperDTO.fromEntity(like));
    }

    @Transactional
    @Override
    public LikeGetResponseDTO get(String gameSlug) {
        UserEntity user = ApiUtil.findUserLogged(userRepository);
        GameEntity game = gameRepository.findBySlug(gameSlug).orElseThrow(() -> new ApiNotFoundException("Game not found"));
        Optional<LikeEntity> optionalLike = likeRepository.findByUserAndGame(user, game);
        LikeEntity like = null;

        if (optionalLike.isPresent()) {
            like = optionalLike.get();
        } else {
            like = new LikeEntity();

            like.setLike(false);
            like.setUser(user);
            like.setGame(game);
        }

        like = likeRepository.save(like);

        return new LikeGetResponseDTO(EntityMapperDTO.fromEntity(like));
    }

}
