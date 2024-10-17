package julioigreja.gamehub.dto.controllers.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record GameUploadRequestDTO(
        @NotBlank(message = "Invalid name")
        @Size(max = 50, min = 3, message = "The name must be between 3 and 50 characters long")
        @Pattern(regexp = "^[a-z0-9 ]+$", message = "Name must contain only lowercase letters, numbers, and spaces")
        String name,

        @NotBlank(message = "Invalid description")
        String description
) implements Serializable {}