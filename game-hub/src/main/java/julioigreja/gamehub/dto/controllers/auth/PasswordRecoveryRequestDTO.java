package julioigreja.gamehub.dto.controllers.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record PasswordRecoveryRequestDTO(
        @Email(message = "Invalid e-mail")
        @Size(max = 100, min = 3, message = "The e-mail must be between 3 and 100 characters long")
        String email
) implements Serializable {}
