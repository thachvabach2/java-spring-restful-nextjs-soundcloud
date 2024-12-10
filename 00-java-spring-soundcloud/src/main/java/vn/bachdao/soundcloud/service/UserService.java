package vn.bachdao.soundcloud.service;

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
}
