package julioigreja.gamehub.dto.controllers.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RegisterRequestDTO(
        @NotBlank(message = "Invalid username")
        @Pattern(regexp = "^[a-z0-9_]*$", message = "Username must be lowercase letters, numbers, and underscores only")
        @Size(max = 20, min = 3, message = "The username must be between 3 and 20 characters long")
        String username,

        @Email(message = "Invalid e-mail")
        @Size(max = 100, min = 3, message = "The e-mail must be between 3 and 100 characters long")
        String email,

        @NotBlank(message = "Invalid password")
        @Size(max = 20, min = 3, message = "The password must be between 3 and 20 characters long")
        String password
) implements Serializable {}
