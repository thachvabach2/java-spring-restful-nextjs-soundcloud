package vn.bachdao.soundcloud.web.rest;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bachdao.soundcloud.domain.Genre;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.GenreService;
import vn.bachdao.soundcloud.service.TrackService;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResCreateTrackDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResGetTrackDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResUpdateTrackDTO;
import vn.bachdao.soundcloud.service.mapper.TrackMapper;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class TrackResource {
    private final TrackService trackService;
    private final UserService userService;
    private final GenreService genreService;
    private final TrackMapper trackMapper;

    public TrackResource(
            TrackService trackService,
            UserService userService,
            GenreService genreService,
            TrackMapper trackMapper) {
        this.trackService = trackService;
        this.userService = userService;
        this.genreService = genreService;
        this.trackMapper = trackMapper;
    }

    @PostMapping("/tracks")
    @ApiMessage("Create a track")
    public ResponseEntity<ResCreateTrackDTO> createTrack(@Valid @RequestBody Track track) throws IdInvalidException {
        // add logged user
        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        if (email.equals("")) {
            throw new IdInvalidException("Bạn chưa đăng nhập");
        }
        User loggedUser = this.userService.getUserByEmail(email);
        track.setUser(loggedUser);

        // check genre exist
        Optional<Genre> genreOptional = this.genreService.fetchGenreById(track.getGenre().getId());
        if (genreOptional.isEmpty()) {
            throw new IdInvalidException("Genre với id = " + track.getGenre().getId() + " không tồn tại");
        }
        track.setGenre(genreOptional.get());

        Track createdTrack = this.trackService.handleCreateTrack(track);
        ResCreateTrackDTO trackDTO = this.trackMapper.trackToResCreateTrackDTO(createdTrack);
        return ResponseEntity.status(HttpStatus.CREATED).body(trackDTO);
    }

    @PutMapping("/tracks")
    @ApiMessage("Update a track by id")
    public ResponseEntity<ResUpdateTrackDTO> updateTrack(@Valid @RequestBody Track reqTrack) throws IdInvalidException {
        // check id
        Optional<Track> trackOptional = this.trackService.fetchTrackById(reqTrack.getId());
        if (trackOptional.isEmpty()) {
            throw new IdInvalidException("Track với id = " + reqTrack.getId() + " không tồn tại");
        }

        Track updatedTrack = this.trackService.handleUpdateTrack(reqTrack, trackOptional.get());
        ResUpdateTrackDTO trackDTO = this.trackMapper.trackToResUpdateTrackDTO(updatedTrack);
        return ResponseEntity.ok(trackDTO);
    }

    @DeleteMapping("/tracks/{id}")
    @ApiMessage("Delete a track by id")
    public ResponseEntity<Void> deleteTrack(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Track> trackOptional = this.trackService.fetchTrackById(id);
        if (trackOptional.isEmpty()) {
            throw new IdInvalidException("Track với id = " + id + " không tồn tại");
        }

        this.trackService.handleDeleteTrack(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/tracks/{id}")
    @ApiMessage("Fetch track by id")
    public ResponseEntity<ResGetTrackDTO> getTrack(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Track> trackOptional = this.trackService.fetchTrackById(id);
        if (trackOptional.isEmpty()) {
            throw new IdInvalidException("Track với id = " + id + " không tồn tại");
        }

        ResGetTrackDTO trackDTO = this.trackMapper.trackToResGetTrackDTO(trackOptional.get());
        return ResponseEntity.ok(trackDTO);
    }

    @GetMapping("/tracks")
    @ApiMessage("Fetch all tracks with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllTracks(@Filter Specification<Track> spec, Pageable pageable) {
        return ResponseEntity.ok(this.trackService.handleGetAllTracks(spec, pageable));
    }
}
