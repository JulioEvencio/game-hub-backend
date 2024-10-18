package julioigreja.gamehub.dto.controllers.game;

import julioigreja.gamehub.dto.entities.GameDTO;
import julioigreja.gamehub.dto.entities.UserDTO;

import java.io.Serializable;

public record GameFindBySlugResponseDTO(
        UserDTO user,
        GameDTO game
) implements Serializable {}
