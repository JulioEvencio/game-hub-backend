package julioigreja.gamehub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import julioigreja.gamehub.dto.controllers.comment.CommentCreateRequestDTO;
import julioigreja.gamehub.dto.controllers.comment.CommentCreateResponseDTO;
import julioigreja.gamehub.exceptions.ApiMessageError;
import julioigreja.gamehub.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/comments")
@Tag(name = "Comment", description = "Endpoints for comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth"),
            summary = "Create a new comment for a game",
            description = "Create a new comment for a game",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Comment created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentCreateResponseDTO.class)
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
    @PostMapping(path = "game/{game-slug}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentCreateResponseDTO> create(@PathVariable(name = "game-slug") String gameSlug, @Valid @RequestBody CommentCreateRequestDTO dto) {
        CommentCreateResponseDTO response = commentService.create(gameSlug, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
