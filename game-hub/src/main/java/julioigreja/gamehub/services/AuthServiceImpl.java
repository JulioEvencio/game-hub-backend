package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.auth.*;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.RoleEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.exceptions.custom.ApiAuthenticationException;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.repositories.RoleRepository;
import julioigreja.gamehub.repositories.UserRepository;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;
    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(RoleRepository roleRepository, UserRepository userRepository, EmailService emailService, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;

        this.emailService = emailService;
        this.jwtService = jwtService;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        this.validateUserExists(dto);

        UserEntity user = this.createUserEntity(dto);

        emailService.sendEmailRegister(user.getUsername(), user.getEmail());

        return new RegisterResponseDTO(
                EntityMapperDTO.fromEntity(user),
                jwtService.createAccessToken(user.getUsername(), List.of("ROLE_USER"))
        );
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new ApiAuthenticationException("E-mail not found"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new ApiAuthenticationException("Invalid password");
        }

        List<String> roles = user.getRoles().stream().map(RoleEntity::getName).toList();

        return new LoginResponseDTO(jwtService.createAccessToken(user.getUsername(), roles));
    }

    @Transactional
    @Override
    public PasswordUpdateResponseDTO passwordUpdate(PasswordUpdateRequestDTO dto) {
        UserEntity user = ApiUtil.findUserLogged(userRepository);

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        userRepository.save(user);

        emailService.sendEmailPasswordUpdate(user.getUsername(), user.getEmail());

        List<String> roles = user.getRoles().stream().map(RoleEntity::getName).toList();

        return new PasswordUpdateResponseDTO(jwtService.createAccessToken(user.getUsername(), roles));
    }

    @Override
    public PasswordRecoveryResponseDTO passwordRecovery(PasswordRecoveryRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new ApiNotFoundException("E-mail not found"));

        List<String> roles = user.getRoles().stream().map(RoleEntity::getName).toList();
        String accessToken = jwtService.createAccessTokenPasswordRecovery(user.getUsername(), roles);

        emailService.sendEmailPasswordRecovery(user.getUsername(), accessToken, user.getEmail());

        return new PasswordRecoveryResponseDTO("Email sent to: " + user.getEmail());
    }

    private void validateUserExists(RegisterRequestDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ApiNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ApiNotFoundException("E-mail already exists");
        }
    }

    @Transactional
    private UserEntity createUserEntity(RegisterRequestDTO dto) {
        UserEntity user = new UserEntity();

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPictureURL("https://api.dicebear.com/9.x/bottts/svg?seed=" + UUID.randomUUID());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCreatedAt(Instant.now());
        user.setGames(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setLikes(new ArrayList<>());

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").orElseThrow(RuntimeException::new));

        user.setRoles(roles);

        return userRepository.save(user);
    }

}
