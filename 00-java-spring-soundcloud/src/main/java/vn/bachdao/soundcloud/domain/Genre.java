package vn.bachdao.soundcloud.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre extends AbstractAuditingEntity {
    @NotBlank(message = "name không được để trống")
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Track> tracks;

}
