package julioigreja.gamehub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import julioigreja.gamehub.dto.controllers.like.LikeGetResponseDTO;
import julioigreja.gamehub.dto.controllers.like.LikeUpdateResponseDTO;
import julioigreja.gamehub.exceptions.ApiMessageError;
import julioigreja.gamehub.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/likes")
@Tag(name = "Like", description = "Endpoints for likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Update game like",
            description = "Update game like",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Like updated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LikeUpdateResponseDTO.class)
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
    @PostMapping(path = "game/{game-slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LikeUpdateResponseDTO> create(@PathVariable(name = "game-slug") String gameSlug) {
        LikeUpdateResponseDTO response = likeService.update(gameSlug);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Get game like",
            description = "Get game like",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Like found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LikeGetResponseDTO.class)
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
    @GetMapping(path = "game/{game-slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LikeGetResponseDTO> get(@PathVariable(name = "game-slug") String gameSlug) {
        LikeGetResponseDTO response = likeService.get(gameSlug);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
