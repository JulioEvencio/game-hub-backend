package julioigreja.gamehub.dto.controllers.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record PasswordUpdateRequestDTO(
        @NotBlank(message = "Invalid password")
        @Size(max = 20, min = 3, message = "The password must be between 3 and 20 characters long")
        String newPassword
) implements Serializable {}
