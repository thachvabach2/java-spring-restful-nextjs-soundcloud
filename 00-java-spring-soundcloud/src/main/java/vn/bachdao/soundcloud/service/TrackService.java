package vn.bachdao.soundcloud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Comment;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.repository.CommentRepository;
import vn.bachdao.soundcloud.repository.TrackRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResGetTrackDTO;
import vn.bachdao.soundcloud.service.mapper.TrackMapper;

@Service
public class TrackService {
    private final TrackRepository trackRepository;

    private final CommentRepository commentRepository;
    private final TrackMapper trackMapper;

    public TrackService(
            TrackRepository trackRepository,
            TrackMapper trackMapper,
            CommentRepository commentRepository) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.commentRepository = commentRepository;
    }

    public Track handleCreateTrack(Track track) {
        return this.trackRepository.save(track);
    }

    public Optional<Track> fetchTrackById(long id) {
        return this.trackRepository.findById(id);
    }

    public Track handleUpdateTrack(Track reqTrack, Track trackDB) {
        trackDB.setId(reqTrack.getId());
        trackDB.setTitle(reqTrack.getTitle());
        trackDB.setDescription(reqTrack.getDescription());
        trackDB.setTrackUrl(reqTrack.getTrackUrl());
        trackDB.setImgUrl(reqTrack.getImgUrl());
        return this.trackRepository.save(trackDB);
    }

    public void handleDeleteTrack(long id) {
        Optional<Track> trackOptional = this.trackRepository.findById(id);
        Track currentTrack = trackOptional.get();

        // delete track (inside playlist_track table)
        currentTrack.getPlaylists().forEach(playlist -> playlist.getTracks().remove(currentTrack));

        // delete track from comment table
        if (currentTrack.getComments() != null) {
            List<Comment> comments = currentTrack.getComments();
            this.commentRepository.deleteAll(comments);
        }

        // delete track from track table
        this.trackRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllTracks(Specification<Track> spec, Pageable pageable) {
        Page<Track> trackPage = this.trackRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();

        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPageNumber(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(trackPage.getTotalPages());
        mt.setTotalElement(trackPage.getTotalElements());

        res.setMeta(mt);
        List<ResGetTrackDTO> trackDTOs = this.trackMapper.trackToResGetTrackDTO(trackPage.getContent());
        res.setData(trackDTOs);

        return res;
    }
}
