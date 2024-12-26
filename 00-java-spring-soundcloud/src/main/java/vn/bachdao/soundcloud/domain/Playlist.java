package vn.bachdao.soundcloud.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "playlists")
@Getter
@Setter
public class Playlist extends AbstractAuditingEntity {
    @NotBlank(message = "Title không được để trống")
    private String title;
    private boolean publics;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User không được để trống")
    private User user;

    @ManyToMany
    @JoinTable(name = "playlist_track", joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "track_id"))
    @JsonIgnoreProperties(value = { "playlists" })
    private List<Track> tracks;
}
