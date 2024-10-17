package julioigreja.gamehub.services;

import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.exceptions.custom.ApiValidationException;
import julioigreja.gamehub.util.ApiUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public void upload(String fileName, String target, MultipartFile file) {
        try {
            Path targetFolder = this.createFolder(target);
            Path targetLocation = targetFolder.resolve(fileName + ApiUtil.getFileExtension(file));

            file.transferTo(targetLocation.toFile());
        } catch (IOException | StringIndexOutOfBoundsException e) {
            throw new ApiValidationException("Invalid file");
        }
    }

    @Override
    public InputStream download(String target) {
        try {
            return new FileInputStream(target);
        } catch (FileNotFoundException e) {
            throw new ApiNotFoundException("File not found");
        }
    }

    private Path createFolder(String nameFolder) {
        Path folder = Paths.get(nameFolder).toAbsolutePath().normalize();

        if (!folder.toFile().exists()) {
            boolean success = folder.toFile().mkdirs();

            if (!success) {
                throw new RuntimeException();
            }
        }

        return folder;
    }

}
