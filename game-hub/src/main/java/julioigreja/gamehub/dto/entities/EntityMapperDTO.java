package julioigreja.gamehub.dto.entities;

import julioigreja.gamehub.entities.*;
import julioigreja.gamehub.util.ApiUtil;

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
                ApiUtil.getLikesTotal(entity.getLikes())
        );
    }

    public static CommentDTO fromEntity(CommentEntity entity) {
        return new CommentDTO(entity.getId(), entity.getContent(), entity.getCreatedAt(), entity.getUser().getUsername());
    }

    public static LikeDTO fromEntity(LikeEntity entity) {
        return new LikeDTO(entity.getId(), entity.isLike(), entity.getUser().getUsername(), entity.getGame().getSlug());
    }

}
