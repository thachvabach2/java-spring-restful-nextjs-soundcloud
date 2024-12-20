package vn.bachdao.soundcloud.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResCreateUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResGetUserDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResUpdateUserDTO;
import vn.bachdao.soundcloud.service.mapper.UserMapper;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResource(UserService userService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user) throws IdInvalidException {
        log.debug("REST request to create User", user);

        // check exist email
        if (userService.existByEmail(user.getEmail()).isPresent()) {
            throw new IdInvalidException("User với email = " + user.getEmail() + " đã tồn tại");
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userMapper.userToResCreateUserDTO(this.userService.createUser(user)));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        log.debug("REST request to update User", reqUser);
        Optional<User> currentUserOptional = this.userService.getUserByID(reqUser.getId());

        // check exist id
        if (currentUserOptional.isEmpty()) {
            throw new IdInvalidException("User với Id = " + reqUser.getId() + " không tồn tại");
        }

        if (reqUser.getPassword() != null) {
            reqUser.setPassword(this.passwordEncoder.encode(reqUser.getPassword()));
        }
        return ResponseEntity
                .ok(this.userMapper
                        .userToResUpdateUserDTO(this.userService.updateUser(reqUser, currentUserOptional.get())));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResGetUserDTO> getUser(@PathVariable("id") long id) throws IdInvalidException {
        Optional<User> userOptional = this.userService.getUserByID(id);

        if (userOptional.isEmpty()) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }

        return ResponseEntity.ok(this.userMapper.userToResGetUserDTO(userOptional.get()));
    }

    @GetMapping("/users")
    @ApiMessage("Get all user with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(Pageable pageable, @Filter Specification<User> spec) {
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        // check id exist
        Optional<User> userOptional = this.userService.getUserByID(id);
        if (userOptional.isEmpty()) {
            throw new IdInvalidException("User với id = " + id + " Không tồn tại");
        }

        this.userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
}