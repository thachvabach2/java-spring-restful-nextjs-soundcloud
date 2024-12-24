package vn.bachdao.soundcloud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.repository.TrackRepository;
import vn.bachdao.soundcloud.repository.UserRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResGetTrackDTO;
import vn.bachdao.soundcloud.service.mapper.TrackMapper;

@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final TrackMapper trackMapper;

    public TrackService(
            TrackRepository trackRepository,
            UserRepository userRepository,
            TrackMapper trackMapper) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.trackMapper = trackMapper;

    }

    public Track handleCreateTrack(Track track) {
        // check user
        Optional<User> userOptional = this.userRepository.findById(track.getUser().getId());
        if (track.getUser() != null) {
            track.setUser(userOptional.isPresent() ? userOptional.get() : null);
        }

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
