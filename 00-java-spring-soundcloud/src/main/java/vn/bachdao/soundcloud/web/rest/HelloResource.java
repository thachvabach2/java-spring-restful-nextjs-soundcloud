package vn.bachdao.soundcloud.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {
    @GetMapping("/")
    public String getHello() {
        return "Hello World from Resourcess";
    }
}
