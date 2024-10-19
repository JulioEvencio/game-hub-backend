package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.user.UserLoggedResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.repositories.UserRepository;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserLoggedResponseDTO findUserLogged() {
        UserEntity user = ApiUtil.findUserLogged(userRepository);

        user.getGames().sort(Comparator.comparing(GameEntity::getCreatedAt).reversed());

        for (GameEntity game : user.getGames()) {
            ApiUtil.generateControllerURL(game);
        }

        return new UserLoggedResponseDTO(
                EntityMapperDTO.fromEntity(user),
                user.getGames().stream().map(EntityMapperDTO::fromEntity).toList()
        );
    }

}
