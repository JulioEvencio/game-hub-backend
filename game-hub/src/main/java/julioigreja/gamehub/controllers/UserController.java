package julioigreja.gamehub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import julioigreja.gamehub.dto.controllers.user.UserFindByUsernameResponseDTO;
import julioigreja.gamehub.dto.controllers.user.UserLoggedResponseDTO;
import julioigreja.gamehub.exceptions.ApiMessageError;
import julioigreja.gamehub.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@Tag(name = "User", description = "Endpoints for users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Find user logged",
            description = "Find user logged",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserLoggedResponseDTO.class)
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
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoggedResponseDTO> findUserLogged() {
        UserLoggedResponseDTO response = userService.findUserLogged();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Find an user by username",
            description = "Find an user by username",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserFindByUsernameResponseDTO.class)
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
    @GetMapping(path = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserFindByUsernameResponseDTO> findByUsername(@PathVariable String username) {
        UserFindByUsernameResponseDTO response = userService.findByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
