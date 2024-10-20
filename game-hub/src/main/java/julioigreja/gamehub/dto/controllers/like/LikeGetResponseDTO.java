package julioigreja.gamehub.dto.controllers.like;

import julioigreja.gamehub.dto.entities.LikeDTO;

import java.io.Serializable;

public record LikeGetResponseDTO(
        LikeDTO like
) implements Serializable {}
