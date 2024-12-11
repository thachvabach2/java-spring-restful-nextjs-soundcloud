package vn.bachdao.soundcloud.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;
import vn.bachdao.soundcloud.web.rest.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.debug("REST request to create User", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(user));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<User> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        log.debug("REST request to update User", reqUser);

        Optional<User> currentUserOptional = this.userService.fetchUserByID(reqUser.getId());

        // check exist id
        if (currentUserOptional.isEmpty()) {
            throw new IdInvalidException("User với Id =" + reqUser.getId() + " không tồn tại");
        }

        return ResponseEntity.ok(this.userService.updateUser(reqUser, currentUserOptional.get()));
    }
}
