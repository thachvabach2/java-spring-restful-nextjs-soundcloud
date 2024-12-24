package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResCreateTrackDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResGetTrackDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.track.ResUpdateTrackDTO;

@Mapper(componentModel = "spring", uses = {})
public interface TrackMapper {
    ResCreateTrackDTO trackToResCreateTrackDTO(Track track);

    ResUpdateTrackDTO trackToResUpdateTrackDTO(Track track);

    ResGetTrackDTO trackToResGetTrackDTO(Track track);

    List<ResGetTrackDTO> trackToResGetTrackDTO(List<Track> tracks);
}
