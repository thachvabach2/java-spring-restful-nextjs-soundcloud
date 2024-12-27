package vn.bachdao.soundcloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.bachdao.soundcloud.domain.Like;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findOneByUserAndTrack(User user, Track track);

    List<Like> findByUser(User user);
}