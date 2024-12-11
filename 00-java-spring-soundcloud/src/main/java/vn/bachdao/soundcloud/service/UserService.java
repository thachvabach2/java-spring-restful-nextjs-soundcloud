package vn.bachdao.soundcloud.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public Optional<User> fetchUserByID(long id) {
        return this.userRepository.findById(id);
    }

    public User updateUser(User reqUser, User currentUser) {
        currentUser.setName(reqUser.getName());
        currentUser.setPassword(reqUser.getPassword());
        currentUser.setGender(reqUser.getGender());
        currentUser.setAge(reqUser.getAge());

        return this.userRepository.save(currentUser);
    }
}
