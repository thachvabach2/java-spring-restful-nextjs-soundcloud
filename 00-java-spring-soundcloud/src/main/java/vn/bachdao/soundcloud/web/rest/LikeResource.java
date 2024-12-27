package vn.bachdao.soundcloud.web.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.bachdao.soundcloud.domain.Like;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.LikeService;
import vn.bachdao.soundcloud.service.TrackService;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResLikedUserTrackDTO;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class LikeResource {

    private final LikeService likeService;
    private final TrackService trackService;
    private final UserService userService;

    public LikeResource(LikeService likeService, TrackService trackService, UserService userService) {
        this.likeService = likeService;
        this.trackService = trackService;
        this.userService = userService;
    }

    @PostMapping("/likes")
    @ApiMessage("Like/Dislike a track")
    public ResponseEntity<Void> handleLikeAndDislikeTrack(@RequestBody Like like) throws IdInvalidException {
        // check user exist
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Hãy đăng nhập");
        }
        User currentUser = this.userService.getUserByEmail(email);

        // check track exist
        Optional<Track> trackOptional = this.trackService.fetchTrackById(like.getTrack().getId());
        if (trackOptional.isEmpty()) {
            throw new IdInvalidException("Track với id = " + like.getTrack().getId() + " không tồn tại");
        }

        like.setUser(currentUser);
        like.setTrack(trackOptional.get());

        this.likeService.handleLikeAndDislikeTrack(like);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/likes")
    @ApiMessage("Fetch tracks liked by a user")
    public List<ResLikedUserTrackDTO> getTracksLikedByUser() {

        return this.likeService.handleGetAllTracksLikedByUser();
    }
}
