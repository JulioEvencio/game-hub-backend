package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GameService {

    GameUploadResponseDTO create(GameUploadRequestDTO dto, MultipartFile picture, MultipartFile file);

}
