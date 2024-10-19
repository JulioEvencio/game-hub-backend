package julioigreja.gamehub.dto.controllers.comment;

import julioigreja.gamehub.dto.entities.CommentDTO;

import java.io.Serializable;

public record CommentCreateResponseDTO(
        CommentDTO comment
) implements Serializable {}
