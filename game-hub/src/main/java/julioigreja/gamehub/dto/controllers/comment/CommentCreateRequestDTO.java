package julioigreja.gamehub.dto.controllers.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record CommentCreateRequestDTO(
        @NotBlank(message = "Invalid content")
        @Size(max = 100, min = 3, message = "The content must be between 3 and 100 characters long")
        String content
) implements Serializable {}
