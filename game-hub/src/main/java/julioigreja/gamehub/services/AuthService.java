package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.auth.RegisterRequestDTO;
import julioigreja.gamehub.dto.controllers.auth.RegisterResponseDTO;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO dto);

}
