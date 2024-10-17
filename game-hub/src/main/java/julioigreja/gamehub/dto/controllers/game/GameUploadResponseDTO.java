package julioigreja.gamehub.dto.controllers.game;

import julioigreja.gamehub.dto.entities.GameDTO;

import java.io.Serializable;

public record GameUploadResponseDTO(
        GameDTO game
) implements Serializable {}
