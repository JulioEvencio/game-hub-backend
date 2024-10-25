package julioigreja.gamehub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import julioigreja.gamehub.dto.controllers.auth.*;
import julioigreja.gamehub.exceptions.ApiMessageError;
import julioigreja.gamehub.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
@Tag(name = "Auth", description = "Endpoints for authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register an user",
            description = "Register an user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registered user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RegisterResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Request error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiMessageError.class)
                            )
                    )
            }
    )
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        RegisterResponseDTO response = authService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Generate jwt token",
            description = "Generate jwt token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Generate jwt token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LoginResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Request error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiMessageError.class)
                            )
                    )
            }
    )
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Update password",
            description = "Update password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated password",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PasswordUpdateResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Request error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiMessageError.class)
                            )
                    )
            }
    )
    @PatchMapping(path = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordUpdateResponseDTO> passwordUpdate(@RequestBody @Valid PasswordUpdateRequestDTO dto) {
        PasswordUpdateResponseDTO response = authService.passwordUpdate(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Password Recovery",
            description = "Send an e-mail to recover the password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "E-mail sent",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PasswordRecoveryResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Request error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiMessageError.class)
                            )
                    )
            }
    )
    @PostMapping(path = "/password-recovery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordRecoveryResponseDTO> passwordRecovery(@RequestBody @Valid PasswordRecoveryRequestDTO dto) {
        PasswordRecoveryResponseDTO response = authService.passwordRecovery(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
