package vn.bachdao.soundcloud.service.dto.repsonse.user;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.bachdao.soundcloud.domain.enumeration.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String email;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private String updatedBy;
}
