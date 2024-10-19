package julioigreja.gamehub.dto.controllers.user;

import julioigreja.gamehub.dto.entities.GameDTO;
import julioigreja.gamehub.dto.entities.UserDTO;

import java.io.Serializable;
import java.util.List;

public record UserFindByUsernameResponseDTO(
        UserDTO user,
        List<GameDTO> games
) implements Serializable {}
