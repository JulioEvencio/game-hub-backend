package julioigreja.gamehub.services;

import julioigreja.gamehub.dto.controllers.auth.*;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

    PasswordUpdateResponseDTO passwordUpdate(PasswordUpdateRequestDTO dto);

    PasswordRecoveryResponseDTO passwordRecovery(PasswordRecoveryRequestDTO dto);

}
