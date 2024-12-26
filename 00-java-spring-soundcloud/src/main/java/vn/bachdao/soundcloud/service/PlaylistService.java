package vn.bachdao.soundcloud.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Playlist;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.repository.PlaylistRepository;
import vn.bachdao.soundcloud.repository.TrackRepository;
import vn.bachdao.soundcloud.repository.UserRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.playlist.ResPlaylistDTO;
import vn.bachdao.soundcloud.service.mapper.PlaylistMapper;

@Service
public class PlaylistService {
    public final PlaylistRepository playlistRepository;
    public final UserRepository userRepository;
    public final TrackRepository trackRepository;
    public final PlaylistMapper playlistMapper;

    public PlaylistService(
            PlaylistRepository playlistRepository,
            UserRepository userRepository,
            TrackRepository trackRepository,
            PlaylistMapper playlistMapper) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.playlistMapper = playlistMapper;
    }

    public ResPlaylistDTO handleCreatePlaylist(Playlist playlist) {
        // check track
        if (playlist.getTracks() != null) {
            List<Long> track_ids = playlist.getTracks().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Track> dbTracks = this.trackRepository.findByIdIn(track_ids);
            playlist.setTracks(dbTracks);
        }

        Playlist savedPlaylist = this.playlistRepository.save(playlist);
        ResPlaylistDTO playlistDTO = this.playlistMapper.playlistToResCreatePlaylistDTO(savedPlaylist);

        return playlistDTO;
    }

    public Optional<Playlist> fetchPlaylistById(long id) {
        return this.playlistRepository.findById(id);
    }

    public ResPlaylistDTO handleUpdatePlaylist(Playlist reqPlaylist, Playlist currPlaylist) {
        currPlaylist.setTitle(reqPlaylist.getTitle());
        currPlaylist.setPublics(reqPlaylist.isPublics());

        Playlist updatedPlaylist = this.playlistRepository.save(currPlaylist);
        ResPlaylistDTO playlistDTO = this.playlistMapper.playlistToResCreatePlaylistDTO(updatedPlaylist);
        return playlistDTO;
    }

    public ResultPaginationDTO handleGetAllPlaylists(Specification<Playlist> spec, Pageable pageable) {
        Page<Playlist> playlistPage = this.playlistRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPageNumber(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(playlistPage.getTotalPages());
        mt.setTotalElement(playlistPage.getTotalElements());

        res.setMeta(mt);
        res.setData(this.playlistMapper.playlistToResCreatePlaylistDTO(playlistPage.getContent()));

        return res;
    }

    public void handleDeletePlaylist(long id) {
        this.playlistRepository.deleteById(id);
    }
}
