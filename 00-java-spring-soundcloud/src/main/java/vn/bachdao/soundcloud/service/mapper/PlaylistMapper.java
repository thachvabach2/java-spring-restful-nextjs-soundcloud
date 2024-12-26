package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import vn.bachdao.soundcloud.domain.Playlist;
import vn.bachdao.soundcloud.service.dto.repsonse.playlist.ResPlaylistDTO;

@Mapper(componentModel = "spring", uses = {})
public interface PlaylistMapper {

    ResPlaylistDTO playlistToResCreatePlaylistDTO(Playlist playlist);

    List<ResPlaylistDTO> playlistToResCreatePlaylistDTO(List<Playlist> playlists);
}
