package vn.bachdao.soundcloud.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserResource {
    @GetMapping("/users")
    public String getUser() {
        return "haha";
    }
}
