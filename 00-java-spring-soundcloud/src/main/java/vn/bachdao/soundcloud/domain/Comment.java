package vn.bachdao.soundcloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends AbstractAuditingEntity {
    private String content;
    private int moment;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "comments" })
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonIgnoreProperties(value = { "comments" })
    @NotNull(message = "Track không được để trống")
    private Track track;
}
