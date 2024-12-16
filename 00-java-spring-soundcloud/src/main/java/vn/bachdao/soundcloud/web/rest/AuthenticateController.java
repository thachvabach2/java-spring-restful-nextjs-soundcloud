package vn.bachdao.soundcloud.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.bachdao.soundcloud.web.rest.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.vm.LoginVM;

@RestController
@RequestMapping("/api/v1")
public class AuthenticateController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticateController(AuthenticationManagerBuilder authenticationManagerBuilder) {

        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    @ApiMessage("Authentication user")
    public ResponseEntity<LoginVM> authorize(@Valid @RequestBody LoginVM loginVM) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword());

        // Xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(loginVM);
    }
}
