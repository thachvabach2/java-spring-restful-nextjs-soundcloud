package vn.bachdao.soundcloud.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResLoginDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.user.ResGetUserDTO;
import vn.bachdao.soundcloud.service.dto.request.ReqLoginDTO;
import vn.bachdao.soundcloud.service.mapper.UserMapper;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthenticateController {

    @Value("${jhipster.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    public AuthenticateController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserService userService,
            SecurityUtils securityUtils,
            UserMapper userMapper) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.userMapper = userMapper;

    }

    @PostMapping("/auth/login")
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
        String access_token = this.securityUtils.createAccessToken(authentication.getName());

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = this.securityUtils.createRefreshToken(authentication.getName());
        this.userService.updateRefreshToken(authentication.getName(), refresh_token);

        // set cookie
        ResponseCookie resCookie = ResponseCookie.from("refresh_token1", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInSeconds)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Get account")
    public ResponseEntity<ResGetUserDTO> getAccount() {

        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        User user = this.userService.getUserByEmail(email);
        return ResponseEntity.ok().body(this.userMapper.userToResGetUserDTO(user));
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get new access token")
    public ResponseEntity<ResLoginDTO> getNewAccessToken(
            @CookieValue(name = "refresh_token1", defaultValue = "abc") String refresh_token)
            throws IdInvalidException {
        if (refresh_token.equals("abc")) {
            throw new IdInvalidException("Bạn không truyền lên token ở cookie");
        }

        // check valid
        Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh Token không hợp lệ");
        }

        // create access token
        String access_token = this.securityUtils.createAccessToken(email);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtils.createRefreshToken(email);
        this.userService.updateRefreshToken(email, new_refresh_token);

        ResponseCookie resCookie = ResponseCookie.from("refresh_token1", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInSeconds)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);

    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout()
            throws IdInvalidException {

        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";

        if (email.equals("")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }

        // update refresh token = null
        this.userService.updateRefreshToken(email, null);

        // remove refresh token cookie
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token1", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }
}
