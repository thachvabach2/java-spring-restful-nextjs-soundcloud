package vn.bachdao.soundcloud.service.dto.repsonse.track;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateTrackDTO {
    private long id;
    private String title;
    private String description;
    private String trackUrl;
    private String imgUrl;
    private int countLike;
    private int countPlay;
    private Instant createdAt;
    private String createdBy;

    private ResUser user;

    @Getter
    @Setter
    public static class ResUser {
        private long id;
        private String email;
    }
}
