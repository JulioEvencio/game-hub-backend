package julioigreja.gamehub.dto.entities;

import julioigreja.gamehub.entities.CommentEntity;
import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.ScreenshotEntity;
import julioigreja.gamehub.entities.UserEntity;

public class EntityMapperDTO {

    public static UserDTO fromEntity(UserEntity entity) {
        return new UserDTO(entity.getId(), entity.getUsername(), entity.getPictureURL(), entity.getCreatedAt());
    }

    public static GameDTO fromEntity(GameEntity entity) {
        return new GameDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSlug(),
                entity.getAmountDownloads(),
                entity.getCreatedAt(),
                entity.getCoverImage().getFileUrl(),
                entity.getFile().getFileUrl(),
                entity.getScreenshots().stream().map(ScreenshotEntity::getFileUrl).toList(),
                (long) entity.getLikes().size()
        );
    }

    public static CommentDTO fromEntity(CommentEntity entity) {
        return new CommentDTO(entity.getId(), entity.getContent(), entity.getCreatedAt());
    }

}
