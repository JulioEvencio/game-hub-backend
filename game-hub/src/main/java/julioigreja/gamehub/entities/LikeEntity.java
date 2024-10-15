package julioigreja.gamehub.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_likes")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_like", nullable = false)
    private boolean like;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

}
