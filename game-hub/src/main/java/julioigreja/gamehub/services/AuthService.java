package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.auth.LoginRequestDTO;
import julioigreja.gamehub.dto.controllers.auth.LoginResponseDTO;
import julioigreja.gamehub.dto.controllers.auth.RegisterRequestDTO;
import julioigreja.gamehub.dto.controllers.auth.RegisterResponseDTO;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

}
