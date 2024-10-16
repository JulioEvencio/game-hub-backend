package julioigreja.gamehub.dto.controllers.auth;

import java.io.Serializable;

public record LoginResponseDTO(
        String accessToken
) implements Serializable {}
