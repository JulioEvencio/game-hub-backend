package julioigreja.gamehub.dto.entities;

import java.io.Serializable;
import java.util.UUID;

public record LikeDTO(
        UUID id,
        Boolean like,
        String user,
        String game
) implements Serializable {}
