package vn.bachdao.soundcloud.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tracks")
@Getter
@Setter
public class Track extends AbstractAuditingEntity {
    @NotBlank(message = "Title không được để trống")
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String trackUrl;
    private String imgUrl;
    private int countLike;
    private int countPlay;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "tracks" })
    private User user;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonIgnoreProperties(value = { "tracks" })
    private Genre genre;

    @ManyToMany(mappedBy = "tracks", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Playlist> playlists;

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "track" })
    private List<Comment> comments;
}
