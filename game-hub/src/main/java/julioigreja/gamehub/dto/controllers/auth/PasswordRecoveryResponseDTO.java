package julioigreja.gamehub.dto.controllers.auth;

import java.io.Serializable;

public record PasswordRecoveryResponseDTO(
        String message
) implements Serializable {}
