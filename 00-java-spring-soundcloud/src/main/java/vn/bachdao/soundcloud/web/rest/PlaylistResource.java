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
import vn.bachdao.soundcloud.domain.Playlist;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.PlaylistService;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.playlist.ResPlaylistDTO;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PlaylistResource {

    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistResource(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @PostMapping("/playlists")
    @ApiMessage("Create a playlist")
    public ResponseEntity<ResPlaylistDTO> createPlaylist(@Valid @RequestBody Playlist playlist)
            throws IdInvalidException {
        // add logged user
        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        if (email.equals("")) {
            throw new IdInvalidException("Bạn chưa đăng nhập");
        }
        User loggedUser = this.userService.getUserByEmail(email);
        playlist.setUser(loggedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.playlistService.handleCreatePlaylist(playlist));
    }

    @PutMapping("/playlists")
    @ApiMessage("Update a playlist by id")
    public ResponseEntity<ResPlaylistDTO> updatePlaylist(@RequestBody Playlist reqPlaylist) throws IdInvalidException {
        // check id
        Optional<Playlist> playlistOptional = this.playlistService.fetchPlaylistById(reqPlaylist.getId());
        if (playlistOptional.isEmpty()) {
            throw new IdInvalidException("Playlist với id = " + reqPlaylist.getId() + " không tồn tại");
        }

        // check title
        if (reqPlaylist.getTitle() == null || reqPlaylist.getTitle().isEmpty()) {
            throw new IdInvalidException("Title không được để trống hoặc bằng null");
        }

        return ResponseEntity.ok(this.playlistService.handleUpdatePlaylist(reqPlaylist, playlistOptional.get()));
    }

    @GetMapping("/playlists")
    @ApiMessage("Fetch all playlist with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllPlaylists(
            @Filter Specification<Playlist> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.playlistService.handleGetAllPlaylists(spec, pageable));
    }

    @DeleteMapping("/playlists/{id}")
    @ApiMessage("Delete a playlist by id")
    public ResponseEntity<Void> deletePlaylist(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Playlist> playlistOptional = this.playlistService.fetchPlaylistById(id);
        if (playlistOptional.isEmpty()) {
            throw new IdInvalidException("Playlist với id = " + id + " không tồn tại");
        }

        this.playlistService.handleDeletePlaylist(id);
        return ResponseEntity.ok(null);
    }
}
