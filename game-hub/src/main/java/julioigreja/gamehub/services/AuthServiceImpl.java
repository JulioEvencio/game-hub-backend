package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.auth.RegisterRequestDTO;
import julioigreja.gamehub.dto.controllers.auth.RegisterResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.RoleEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.repositories.RoleRepository;
import julioigreja.gamehub.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(RoleRepository roleRepository, UserRepository userRepository, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ApiNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ApiNotFoundException("E-mail already exists");
        }

        UserEntity user = new UserEntity();

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPictureURL("https://api.dicebear.com/9.x/bottts/svg?seed=" + UUID.randomUUID());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setGames(new ArrayList<>());

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").orElseThrow(RuntimeException::new));

        user.setRoles(roles);

        return new RegisterResponseDTO(
                EntityMapperDTO.fromEntity(userRepository.save(user)),
                jwtService.createAccessToken(user.getUsername(), List.of("ROLE_USER"))
        );
    }

}
