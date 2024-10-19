package julioigreja.gamehub.util;

import julioigreja.gamehub.entities.GameEntity;
import julioigreja.gamehub.entities.UserEntity;
import julioigreja.gamehub.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

public class ApiUtil {

    public static UserEntity findUserLogged(UserRepository userRepository) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public static void generateControllerURL(GameEntity game) {
        String serverURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String controllerCoverImage = "/api/games/cover-image/";
        String controllerFile = "/api/games/file/";
        String controllerScreenshot = "/api/games/screenshot/";

        game.getCoverImage().setFileUrl(serverURL + controllerCoverImage + game.getSlug());
        game.getFile().setFileUrl(serverURL + controllerFile + game.getSlug());

        game.getScreenshots().forEach(screenshot -> screenshot.setFileUrl(serverURL + controllerScreenshot + game.getSlug() + "/" + screenshot.getId()));
    }

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());

        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    public static String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

}
