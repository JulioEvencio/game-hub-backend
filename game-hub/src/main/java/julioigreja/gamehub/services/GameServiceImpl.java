package julioigreja.gamehub.services;

import jakarta.transaction.Transactional;
import julioigreja.gamehub.dto.controllers.game.GameFindAllResponseDTO;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final String fileDirectory;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final CoverImageRepository coverImageRepository;
    private final FileRepository fileRepository;
    private final ScreenshotRepository screenshotRepository;

    private final FileService fileService;
    private final EmailService emailService;

    private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private static final String ZIP_TYPE = "application/zip";

    public GameServiceImpl(@Value("${api.file.directory}") String fileDirectory, UserRepository userRepository, GameRepository gameRepository, CoverImageRepository coverImageRepository, FileRepository fileRepository, ScreenshotRepository screenshotRepository, FileService fileService, EmailService emailService) {
        this.fileDirectory = fileDirectory;

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.coverImageRepository = coverImageRepository;
        this.fileRepository = fileRepository;
        this.screenshotRepository = screenshotRepository;

        this.fileService = fileService;
        this.emailService = emailService;
    }

    @Override
    public GameFindBySlugResponseDTO findBySlug(String slug) {
        GameEntity game = gameRepository.findBySlug(slug).orElseThrow(() -> new ApiNotFoundException("Game not found"));

        ApiUtil.generateControllerURL(game);

        return new GameFindBySlugResponseDTO(
                EntityMapperDTO.fromEntity(game.getUser()),
                EntityMapperDTO.fromEntity(game),
                game.getComments().stream().map(EntityMapperDTO::fromEntity).toList()
        );
    }

    @Override
    public GameFindAllResponseDTO findAll(Pageable pageable) {
        Page<GameEntity> games = gameRepository.findAll(pageable);

        for (GameEntity game : games) {
            ApiUtil.generateControllerURL(game);
        }

        return new GameFindAllResponseDTO(games.map(EntityMapperDTO::fromEntity));
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

        emailService.sendEmailGamePublished(game.getUser().getUsername(), game.getName(), game.getSlug(), game.getUser().getEmail());

        return new GameUploadResponseDTO(EntityMapperDTO.fromEntity(game));
    }

    @Override
    public InputStreamResource downloadCoverImage(String gameSlug) {
        CoverImageEntity coverImage = coverImageRepository.findByGame_Slug(gameSlug).orElseThrow(() -> new ApiNotFoundException("Cover image not found"));
        InputStream inputStream = fileService.download(coverImage.getFileUrl());

        return new InputStreamResource(inputStream);
    }

    @Transactional
    @Override
    public InputStreamResource downloadFile(String gameSlug) {
        FileEntity file = fileRepository.findByGame_Slug(gameSlug).orElseThrow(() -> new ApiNotFoundException("File not found"));
        InputStream inputStream = fileService.download(file.getFileUrl());

        GameEntity game = file.getGame();
        game.setAmountDownloads(game.getAmountDownloads() + 1L);
        gameRepository.save(game);

        return new InputStreamResource(inputStream);
    }

    @Override
    public InputStreamResource downloadScreenshot(String gameSlug, UUID screenshotUUID) {
        ScreenshotEntity screenshot = screenshotRepository.findByGame_SlugAndId(gameSlug, screenshotUUID).orElseThrow(() -> new ApiNotFoundException("Screenshot not found"));
        InputStream inputStream = fileService.download(screenshot.getFileUrl());

        return new InputStreamResource(inputStream);
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
        UserEntity user = ApiUtil.findUserLogged(userRepository);
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

}
