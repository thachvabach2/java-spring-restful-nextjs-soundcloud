package vn.bachdao.soundcloud.service.dto.repsonse.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCommentDTO {
    private long id;
    private String content;
    private int moment;
    private boolean deleted;

    private ResUser user;
    private ResTrack track;

    @Getter
    @Setter
    public static class ResUser {
        private long id;
        private String email;
    }

    @Getter
    @Setter
    public static class ResTrack {
        private long id;
        private String title;
    }
}
