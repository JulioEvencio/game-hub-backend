package julioigreja.gamehub.dto.controllers.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record GameUploadRequestDTO(
        @NotBlank(message = "Invalid name")
        @Size(max = 50, min = 3, message = "The name must be between 3 and 50 characters long")
        @Pattern(regexp = "^[a-z0-9_ ]+$", message = "Name must contain only lowercase letters, numbers, spaces, and underscores")
        String name,

        @NotBlank(message = "Invalid description")
        @Size(max = 10000, min = 3, message = "The name must be between 3 and 50 characters long")
        String description
) implements Serializable {}
