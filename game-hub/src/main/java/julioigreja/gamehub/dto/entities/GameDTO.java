package julioigreja.gamehub.dto.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record GameDTO(
        UUID id,
        String name,
        String description,
        String slug,
        Long amountDownloads,
        Instant createdAt,
        String picture,
        String file,
        Long likes
) implements Serializable {}
