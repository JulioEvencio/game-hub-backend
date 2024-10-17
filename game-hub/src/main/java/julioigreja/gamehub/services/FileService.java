package julioigreja.gamehub.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {

    void upload(String fileName, String target, MultipartFile file);

    InputStream download(String target);

}
