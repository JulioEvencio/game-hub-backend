package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.FileEntity;
import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.exceptions.custom.ApiValidationException;
import julioigreja.gamehub.repositories.FileRepository;
import julioigreja.gamehub.repositories.GameRepository;
import julioigreja.gamehub.repositories.UserRepository;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final String fileDirectory;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final FileRepository fileRepository;

    private final FileService fileService;

    private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private static final String ZIP_TYPE = "application/zip";

    public GameServiceImpl(@Value("${api.file.directory}") String fileDirectory, UserRepository userRepository, GameRepository gameRepository, FileRepository fileRepository, FileService fileService) {
        this.fileDirectory = fileDirectory;

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.fileRepository = fileRepository;

        this.fileService = fileService;
    }

    @Override
    public GameUploadResponseDTO create(GameUploadRequestDTO dto, MultipartFile picture, MultipartFile file) {
        this.validateGame(dto, picture, file);

        String gameSlug = ApiUtil.generateSlug(dto.name());
        String targetFile = fileDirectory + gameSlug;

        fileService.upload(gameSlug, targetFile, picture);
        fileService.upload(gameSlug, targetFile, file);

        FileEntity pictureEntity = this.createFileEntity(ApiUtil.getFileExtension(picture), targetFile + "/" + gameSlug + ApiUtil.getFileExtension(picture));
        FileEntity fileEntity = this.createFileEntity(ApiUtil.getFileExtension(file), targetFile + "/" + gameSlug + ApiUtil.getFileExtension(file));

        GameEntity game = this.createGameEntity(dto, pictureEntity, fileEntity);

        this.generateControllerURL(game);

        return new GameUploadResponseDTO(EntityMapperDTO.fromEntity(game));
    }

    private void validateGame(GameUploadRequestDTO dto, MultipartFile picture, MultipartFile file) {
        if (gameRepository.findByName(dto.name()).isPresent()) {
            throw new ApiValidationException("Name already exists");
        }

        if (picture == null || picture.getOriginalFilename() == null) {
            throw new ApiValidationException("Invalid picture");
        }

        if (!IMAGE_TYPES.contains(picture.getContentType())) {
            throw new ApiValidationException("The picture must be an image file (JPEG, JPG, PNG)");
        }

        if (file == null || file.getOriginalFilename() == null) {
            throw new ApiValidationException("Invalid file");
        }

        if (!ZIP_TYPE.equals(file.getContentType())) {
            throw new ApiValidationException("The file must be a ZIP archive");
        }
    }

    @Transactional
    private GameEntity createGameEntity(GameUploadRequestDTO dto, FileEntity picture, FileEntity file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        GameEntity game = new GameEntity();

        game.setName(dto.name());
        game.setDescription(dto.description());
        game.setSlug(ApiUtil.generateSlug(game.getName()));
        game.setAmountDownloads(0L);
        game.setCreatedAt(Instant.now());
        game.setPicture(picture);
        game.setFile(file);
        game.setComments(new ArrayList<>());
        game.setLikes(new ArrayList<>());
        game.setUser(user);

        fileRepository.save(picture);
        fileRepository.save(file);

        return gameRepository.save(game);
    }

    private FileEntity createFileEntity(String type, String url) {
        FileEntity file = new FileEntity();

        file.setFileType(type);
        file.setFileUrl(url);

        return file;
    }

    private void generateControllerURL(GameEntity game) {
        String serverURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String controllerPicture = "/api/games/picture/";
        String controllerFile = "/api/games/file/";

        game.getPicture().setFileUrl(serverURL + controllerPicture + game.getSlug());
        game.getFile().setFileUrl(serverURL + controllerFile + game.getSlug());
    }

}
