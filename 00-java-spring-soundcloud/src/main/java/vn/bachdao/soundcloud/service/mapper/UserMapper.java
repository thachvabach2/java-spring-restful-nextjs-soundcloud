package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResCreateUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResGetUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResUpdateUserDTO;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {
    ResCreateUserDTO userToResCreateUserDTO(User user);

    ResUpdateUserDTO userToResUpdateUserDTO(User user);

    @Mapping(target = "track_ids", source = "tracks")
    ResGetUserDTO userToResGetUserDTO(User user);

    default long mapTrackToLong(Track track) {
        return track.getId();
    }

    List<ResGetUserDTO> userToResGetUserDTO(List<User> users);
}