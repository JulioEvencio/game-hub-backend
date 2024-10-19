package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.comment.CommentCreateRequestDTO;
import julioigreja.gamehub.dto.controllers.comment.CommentCreateResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.CommentEntity;
import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.repositories.CommentRepository;
import julioigreja.gamehub.repositories.GameRepository;
import julioigreja.gamehub.repositories.UserRepository;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(UserRepository userRepository, GameRepository gameRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public CommentCreateResponseDTO create(String gameSlug, CommentCreateRequestDTO dto) {
        UserEntity user = ApiUtil.findUserLogged(userRepository);
        GameEntity game = gameRepository.findBySlug(gameSlug).orElseThrow(() -> new ApiNotFoundException("Game not found"));
        CommentEntity comment = new CommentEntity();

        comment.setContent(dto.content());
        comment.setCreatedAt(Instant.now());
        comment.setUser(user);
        comment.setGame(game);

        comment = commentRepository.save(comment);

        return new CommentCreateResponseDTO(EntityMapperDTO.fromEntity(comment));
    }

}
