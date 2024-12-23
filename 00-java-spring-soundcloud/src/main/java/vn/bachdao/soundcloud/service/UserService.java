package vn.bachdao.soundcloud.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.repository.UserRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.mapper.UserMapper;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    public Optional<User> existByEmail(String email) {
        return this.userRepository.findOneByEmailIgnoreCase(email);
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public Optional<User> getUserByID(long id) {
        return this.userRepository.findById(id);
    }

    public User updateUser(User reqUser, User currentUser) {
        currentUser.setName(reqUser.getName());
        currentUser.setPassword(reqUser.getPassword());
        currentUser.setGender(reqUser.getGender());
        currentUser.setAge(reqUser.getAge());

        return this.userRepository.save(currentUser);
    }

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPageNumber(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(userPage.getTotalPages());
        mt.setTotalElement(userPage.getTotalElements());
        rs.setMeta(mt);

        rs.setData(this.userMapper.userToResGetUserDTO(userPage.getContent()));

        return rs;
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User updateRefreshToken(String email, String refresh_token) {
        User currentUser = this.userRepository.findByEmail(email);
        currentUser.setRefreshToken(refresh_token);
        return this.userRepository.save(currentUser);
    }
}
