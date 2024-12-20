package vn.bachdao.soundcloud.service.dto.repsonse.user;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.bachdao.soundcloud.domain.enumeration.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String email;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private String createdBy;
}
