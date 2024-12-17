package vn.bachdao.soundcloud.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.dto.ResLoginDTO;
import vn.bachdao.soundcloud.web.rest.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.vm.LoginVM;

@RestController
@RequestMapping("/api/v1")
public class AuthenticateController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;

    public AuthenticateController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtils securityUtils) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/login")
    @ApiMessage("Login user")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginVM loginVM) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword());

        // Xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // lưu vào Spring security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create token
        String access_token = this.securityUtils.createAccessToken(authentication);
        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);

        // add header authorization
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(access_token);

        return ResponseEntity.ok()
                .body(res);
    }
}
