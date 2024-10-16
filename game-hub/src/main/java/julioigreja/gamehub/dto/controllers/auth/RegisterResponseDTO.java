package julioigreja.gamehub.dto.controllers.auth;

import julioigreja.gamehub.dto.entities.UserDTO;

import java.io.Serializable;

public record RegisterResponseDTO(
        UserDTO user,
        String accessToken
) implements Serializable {}
