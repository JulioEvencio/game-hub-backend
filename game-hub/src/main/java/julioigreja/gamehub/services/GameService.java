package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.game.GameFindAllResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameFindBySlugResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface GameService {

    GameFindBySlugResponseDTO findBySlug(String slug);

    GameFindAllResponseDTO findAll(Pageable pageable);

    GameUploadResponseDTO create(GameUploadRequestDTO dto, MultipartFile coverImage, MultipartFile file, List<MultipartFile> screenshots);

    InputStreamResource downloadCoverImage(String gameSlug);

    InputStreamResource downloadFile(String gameSlug);

    InputStreamResource downloadScreenshot(String gameSlug, UUID screenshotUUID);

}
