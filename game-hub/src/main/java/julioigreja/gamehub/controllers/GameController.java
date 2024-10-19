package julioigreja.gamehub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import julioigreja.gamehub.dto.controllers.game.GameFindAllResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameFindBySlugResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import julioigreja.gamehub.exceptions.ApiMessageError;
import julioigreja.gamehub.services.GameService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/games")
@Tag(name = "Game", description = "Endpoints for games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(
            summary = "Find a game by slug",
            description = "Find a game by slug",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Game found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameFindBySlugResponseDTO.class)
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
    @GetMapping(path = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameFindBySlugResponseDTO> findBySlug(@PathVariable String slug) {
        GameFindBySlugResponseDTO response = gameService.findBySlug(slug);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Find all games",
            description = "Find all games",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All games returned",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameFindAllResponseDTO.class)
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameFindAllResponseDTO> findAll(@PageableDefault Pageable pageable) {
        GameFindAllResponseDTO response = gameService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Create a new game",
            description = "Create a new game",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Game created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameUploadResponseDTO.class)
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameUploadResponseDTO> create(@Valid @ModelAttribute GameUploadRequestDTO dto, @RequestPart MultipartFile coverImage, @RequestPart MultipartFile file, @RequestPart List<MultipartFile> screenshots) {
        GameUploadResponseDTO response = gameService.create(dto, coverImage, file, screenshots);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Download a game cover image",
            description = "Download a game cover image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "downloaded cover image",
                            content = {
                                    @Content(
                                            mediaType = MediaType.IMAGE_PNG_VALUE,
                                            schema = @Schema(implementation = InputStreamResource.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.IMAGE_JPEG_VALUE,
                                            schema = @Schema(implementation = InputStreamResource.class)
                                    )
                            }
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
    @GetMapping(path = "/cover-image/{game-slug}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    public ResponseEntity<InputStreamResource> downloadCoverImage(@PathVariable(name = "game-slug") String gameSlug) {
        InputStreamResource response = gameService.downloadCoverImage(gameSlug);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Download a game file",
            description = "Download a game file",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "downloaded file",
                            content = @Content(
                                    mediaType = "application/zip",
                                    schema = @Schema(implementation = InputStreamResource.class)
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
    @GetMapping(path = "/file/{game-slug}", produces = "application/zip")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(name = "game-slug") String gameSlug) {
        InputStreamResource response = gameService.downloadFile(gameSlug);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Download a game screenshot",
            description = "Download a game screenshot",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "downloaded screenshot",
                            content = {
                                    @Content(
                                            mediaType = MediaType.IMAGE_PNG_VALUE,
                                            schema = @Schema(implementation = InputStreamResource.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.IMAGE_JPEG_VALUE,
                                            schema = @Schema(implementation = InputStreamResource.class)
                                    )
                            }
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
    @GetMapping(path = "/screenshot/{game-slug}/{screenshot-uuid}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    public ResponseEntity<InputStreamResource> downloadScreenshot(@PathVariable(name = "game-slug") String gameSlug, @PathVariable(name = "screenshot-uuid") UUID screenshotUUID) {
        InputStreamResource response = gameService.downloadScreenshot(gameSlug, screenshotUUID);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
