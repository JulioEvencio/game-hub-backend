package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.game.GameFindAllResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameFindBySlugResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {

    GameFindBySlugResponseDTO findBySlug(String slug);

    GameFindAllResponseDTO findAll(Pageable pageable);

    GameUploadResponseDTO create(GameUploadRequestDTO dto, MultipartFile coverImage, MultipartFile file, List<MultipartFile> screenshots);

}
