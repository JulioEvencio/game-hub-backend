package julioigreja.gamehub.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "file_type", nullable = false, length = 4)
    private String fileType;

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
