package julioigreja.gamehub.dto.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record CommentDTO(
        UUID id,
        String content,
        Instant createdAt
) implements Serializable {}
