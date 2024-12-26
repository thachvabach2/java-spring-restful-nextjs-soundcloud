package vn.bachdao.soundcloud.service.dto.repsonse.playlist;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPlaylistDTO {
    private long id;
    private String title;
    private boolean publics;

    private ResUser user;
    private List<ResTrack> tracks;

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
