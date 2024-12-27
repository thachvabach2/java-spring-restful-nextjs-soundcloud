package vn.bachdao.soundcloud.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Like;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.repository.LikeRepository;
import vn.bachdao.soundcloud.repository.UserRepository;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResLikedUserTrackDTO;
import vn.bachdao.soundcloud.service.mapper.TrackMapper;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TrackMapper trackMapper;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, TrackMapper trackMapper) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.trackMapper = trackMapper;
    }

    public Like handleCreateLike(Like like) {
        like.setQuantity(0);
        return this.likeRepository.save(like);
    }

    public Like handleLikeAndDislikeTrack(Like like) {
        // check exist like by user and track, if not exist create like
        Like currLike = this.likeRepository.findOneByUserAndTrack(like.getUser(), like.getTrack());
        if (currLike == null) {
            // create like
            currLike = this.handleCreateLike(like);
        }

        // revert like/dislike
        currLike.setQuantity(currLike.getQuantity() != 1 ? 1 : -1);

        return this.likeRepository.save(currLike);
    }

    public List<ResLikedUserTrackDTO> handleGetAllTracksLikedByUser() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User loggedUser = this.userRepository.findByEmail(email);

        List<Like> likes = this.likeRepository.findByUser(loggedUser);
        List<Track> tracks = likes.stream().filter(like -> like.getQuantity() == 1)
                .map(like -> like.getTrack()).collect(Collectors.toList());

        return this.trackMapper.trackToResLikedUserTrackDTOs(tracks);
    }
}
