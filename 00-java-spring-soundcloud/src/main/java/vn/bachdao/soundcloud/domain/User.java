package vn.bachdao.soundcloud.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.bachdao.soundcloud.domain.enumeration.GenderEnum;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractAuditingEntity {

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Password không được để trống")
    private String password;

    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" })
    private List<Track> tracks;
}
