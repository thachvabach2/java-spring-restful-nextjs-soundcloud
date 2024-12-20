package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResCreateUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResGetUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResUpdateUserDTO;

@Mapper(componentModel = "spring", uses = {})
@Service
public interface UserMapper {
    ResCreateUserDTO userToResCreateUserDTO(User user);

    ResUpdateUserDTO userToResUpdateUserDTO(User user);

    ResGetUserDTO userToResGetUserDTO(User user);

    List<ResGetUserDTO> userToResGetUserDTO(List<User> users);
}