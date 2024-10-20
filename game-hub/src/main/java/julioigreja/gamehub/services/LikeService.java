package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.like.LikeUpdateResponseDTO;
import julioigreja.gamehub.dto.controllers.like.LikeGetResponseDTO;

public interface LikeService {

    LikeUpdateResponseDTO update(String gameSlug);

    LikeGetResponseDTO get(String gameSlug);

}
