package julioigreja.gamehub.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class ApiUtil {

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());

        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    public static String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

}
