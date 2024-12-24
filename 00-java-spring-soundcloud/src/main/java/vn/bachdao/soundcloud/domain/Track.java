package vn.bachdao.soundcloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
}
