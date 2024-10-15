package julioigreja.gamehub.dto.entities;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String pictureURL
) implements Serializable {}