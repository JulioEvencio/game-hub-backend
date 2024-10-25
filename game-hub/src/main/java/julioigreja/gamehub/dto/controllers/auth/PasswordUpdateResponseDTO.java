package julioigreja.gamehub.dto.controllers.auth;

import java.io.Serializable;

public record PasswordUpdateResponseDTO(
        String accessToken
) implements Serializable {}
