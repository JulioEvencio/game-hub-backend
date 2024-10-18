package julioigreja.gamehub.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_games")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = false, length = 10000)
    private String description;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(name = "amount_downloads", nullable = false)
    private Long amountDownloads;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToOne(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private CoverImageEntity coverImage;

    @OneToOne(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private FileEntity file;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ScreenshotEntity> screenshots;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<LikeEntity> likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getAmountDownloads() {
        return amountDownloads;
    }

    public void setAmountDownloads(Long amountDownloads) {
        this.amountDownloads = amountDownloads;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public CoverImageEntity getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CoverImageEntity coverImage) {
        this.coverImage = coverImage;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public List<ScreenshotEntity> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<ScreenshotEntity> screenshots) {
        this.screenshots = screenshots;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<LikeEntity> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeEntity> likes) {
        this.likes = likes;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
