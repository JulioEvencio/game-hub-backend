package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.comment.CommentCreateRequestDTO;
import julioigreja.gamehub.dto.controllers.comment.CommentCreateResponseDTO;

public interface CommentService {

    CommentCreateResponseDTO create(String gameSlug, CommentCreateRequestDTO dto);

}
