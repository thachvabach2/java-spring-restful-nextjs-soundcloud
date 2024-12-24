package vn.bachdao.soundcloud.service.dto.repsonse.user;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.bachdao.soundcloud.domain.enumeration.GenderEnum;

@Getter
@Setter
public class ResGetUserDTO {
    private long id;
    private String email;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private List<Long> track_ids;
}
