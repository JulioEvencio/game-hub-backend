package julioigreja.gamehub.dto.controllers.game;

import julioigreja.gamehub.dto.entities.CommentDTO;
import julioigreja.gamehub.dto.entities.GameDTO;
import julioigreja.gamehub.dto.entities.UserDTO;

import java.io.Serializable;
import java.util.List;

public record GameFindBySlugResponseDTO(
        UserDTO user,
        GameDTO game,
        List<CommentDTO> comments
) implements Serializable {}
