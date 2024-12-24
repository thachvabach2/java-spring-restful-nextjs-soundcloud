package vn.bachdao.soundcloud.service.dto.repsonse.track;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateTrackDTO {
    private long id;
    private String title;
    private String description;
    private String trackUrl;
    private String imgUrl;
    private Instant updatedAt;
    private String updatedBy;

    private ResUser user;

    @Getter
    @Setter
    public static class ResUser {
        private long id;
        private String email;
    }
}
