package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.game.GameFindBySlugResponseDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadRequestDTO;
import julioigreja.gamehub.dto.controllers.game.GameUploadResponseDTO;
import julioigreja.gamehub.dto.entities.EntityMapperDTO;
import julioigreja.gamehub.entities.*;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.exceptions.custom.ApiValidationException;
import julioigreja.gamehub.repositories.*;
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

    private final FileService fileService;

    private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private static final String ZIP_TYPE = "application/zip";

    public GameServiceImpl(@Value("${api.file.directory}") String fileDirectory, UserRepository userRepository, GameRepository gameRepository, FileService fileService) {
        this.fileDirectory = fileDirectory;

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;

        this.fileService = fileService;
    }

    @Transactional
    @Override
    public GameFindBySlugResponseDTO findBySlug(String slug) {
        GameEntity game = gameRepository.findBySlug(slug).orElseThrow(() -> new ApiNotFoundException("Game not found"));

        this.generateControllerURL(game);

        return new GameFindBySlugResponseDTO(
                EntityMapperDTO.fromEntity(game.getUser()),
                EntityMapperDTO.fromEntity(game)
        );
    }

    @Override
    public GameUploadResponseDTO create(GameUploadRequestDTO dto, MultipartFile coverImage, MultipartFile file, List<MultipartFile> screenshots) {
        this.validateGame(dto, coverImage, file, screenshots);

        String gameSlug = ApiUtil.generateSlug(dto.name());
        String targetFile = fileDirectory + gameSlug;

        fileService.upload(gameSlug, targetFile, coverImage);
        CoverImageEntity coverImageEntity = this.createCoverImageEntity(ApiUtil.getFileExtension(coverImage), targetFile + "/" + gameSlug + ApiUtil.getFileExtension(coverImage));

        fileService.upload(gameSlug, targetFile, file);
        FileEntity fileEntity = this.createFileEntity(ApiUtil.getFileExtension(file), targetFile + "/" + gameSlug + ApiUtil.getFileExtension(file));

        List<ScreenshotEntity> screenshotEntities = new ArrayList<>();

        for (int i = 0; i < screenshots.size(); i++) {
            String fileName = gameSlug + "_" + i;
            MultipartFile screenshot = screenshots.get(i);

            fileService.upload(fileName, targetFile, screenshot);
            screenshotEntities.add(this.createScreenshotEntity(ApiUtil.getFileExtension(screenshot), targetFile + "/" + fileName + ApiUtil.getFileExtension(screenshot)));
        }

        GameEntity game = this.createGameEntity(dto, coverImageEntity, fileEntity, screenshotEntities);

        this.generateControllerURL(game);

        return new GameUploadResponseDTO(EntityMapperDTO.fromEntity(game));
    }

    private void validateGame(GameUploadRequestDTO dto, MultipartFile coverImage, MultipartFile file, List<MultipartFile> screenshots) {
        if (gameRepository.findByName(dto.name()).isPresent()) {
            throw new ApiValidationException("Name already exists");
        }

        if (coverImage == null || coverImage.getOriginalFilename() == null) {
            throw new ApiValidationException("Invalid cover image");
        }

        if (!IMAGE_TYPES.contains(coverImage.getContentType())) {
            throw new ApiValidationException("The cover image must be an image file (JPEG, JPG, PNG)");
        }

        if (file == null || file.getOriginalFilename() == null) {
            throw new ApiValidationException("Invalid file");
        }

        if (!ZIP_TYPE.equals(file.getContentType())) {
            throw new ApiValidationException("The file must be a ZIP archive");
        }

        for (MultipartFile screenshot : screenshots) {
            if (screenshot == null || screenshot.getOriginalFilename() == null) {
                throw new ApiValidationException("Invalid screenshot");
            }

            if (!IMAGE_TYPES.contains(screenshot.getContentType())) {
                throw new ApiValidationException("The screenshot must be an image file (JPEG, JPG, PNG)");
            }
        }
    }

    @Transactional
    private GameEntity createGameEntity(GameUploadRequestDTO dto, CoverImageEntity coverImage, FileEntity file, List<ScreenshotEntity> screenshots) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        GameEntity game = new GameEntity();

        game.setName(dto.name());
        game.setDescription(dto.description());
        game.setSlug(ApiUtil.generateSlug(game.getName()));
        game.setAmountDownloads(0L);
        game.setCreatedAt(Instant.now());
        game.setCoverImage(coverImage);
        game.setFile(file);
        game.setScreenshots(screenshots);
        game.setComments(new ArrayList<>());
        game.setLikes(new ArrayList<>());
        game.setUser(user);

        coverImage.setGame(game);
        file.setGame(game);

        for (ScreenshotEntity screenshot : screenshots) {
            screenshot.setGame(game);
        }

        return gameRepository.save(game);
    }

    private CoverImageEntity createCoverImageEntity(String type, String url) {
        CoverImageEntity coverImage = new CoverImageEntity();

        coverImage.setFileType(type);
        coverImage.setFileUrl(url);

        return coverImage;
    }

    private FileEntity createFileEntity(String type, String url) {
        FileEntity file = new FileEntity();

        file.setFileType(type);
        file.setFileUrl(url);

        return file;
    }

    private ScreenshotEntity createScreenshotEntity(String type, String url) {
        ScreenshotEntity screenshot = new ScreenshotEntity();

        screenshot.setFileType(type);
        screenshot.setFileUrl(url);

        return screenshot;
    }

    private void generateControllerURL(GameEntity game) {
        String serverURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String controllerCoverImage = "/api/games/picture/";
        String controllerFile = "/api/games/file/";
        String controllerScreenshot = "/api/games/screenshot/";

        game.getCoverImage().setFileUrl(serverURL + controllerCoverImage + game.getSlug());
        game.getFile().setFileUrl(serverURL + controllerFile + game.getSlug());

        game.getScreenshots().forEach(screenshot -> screenshot.setFileUrl(serverURL + controllerScreenshot + game.getSlug() + "/" + screenshot.getId()));
    }

}
