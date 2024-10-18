package julioigreja.gamehub.dto.controllers.game;

import julioigreja.gamehub.dto.entities.GameDTO;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public record GameFindAllResponseDTO(
        Page<GameDTO> games
) implements Serializable {}
