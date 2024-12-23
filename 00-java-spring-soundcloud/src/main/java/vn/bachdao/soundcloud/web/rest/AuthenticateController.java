package vn.bachdao.soundcloud.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResLoginDTO;
import vn.bachdao.soundcloud.service.dto.request.ReqLoginDTO;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthenticateController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    @Value("${jhipster.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    public AuthenticateController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtils securityUtils,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiMessage("Login user")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginVM) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword());

        // Xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // lưu vào Spring security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create access token
        String access_token = this.securityUtils.createAccessToken(authentication);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = this.securityUtils.createRefreshToken(authentication.getName());
        this.userService.updateRefreshToken(authentication.getName(), refresh_token);

        ResponseCookie springCookie = ResponseCookie.from("refresh_token1", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInSeconds)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(res);
    }
}
