package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.user.UserFindByUsernameResponseDTO;
import julioigreja.gamehub.dto.controllers.user.UserLoggedResponseDTO;

public interface UserService {

    UserLoggedResponseDTO findUserLogged();

    UserFindByUsernameResponseDTO findByUsername(String username);

}
