package julioigreja.gamehub.dto.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GameDTO(
        UUID id,
        String name,
        String description,
        String slug,
        Long amountDownloads,
        Instant createdAt,
        String coverImageUrl,
        String fileUrl,
        List<String> screenshotsUrl,
        Long likes
) implements Serializable {}
