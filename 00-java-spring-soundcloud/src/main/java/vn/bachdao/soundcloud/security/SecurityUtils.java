package vn.bachdao.soundcloud.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public final class SecurityUtils {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    private final JwtEncoder jwtEncoder;

    @Value("${jhipster.security.authentication.jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    public SecurityUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(String email) {
        Instant now = Instant.now();

        Instant validity = now.plus(this.accessTokenValidityInSeconds, ChronoUnit.SECONDS);

        // hard code
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_USER_CREATE");
        authorities.add("ROLE_USER_UPDATE");

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("auth", authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        // signature = secret key + jwsHeader(header) + claims(payload)
        // secret key = jwtKey(application) + alg
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String email) {
        Instant now = Instant.now();

        Instant validity = now.plus(this.refreshTokenValidityInSeconds, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        // signature = secret key + jwsHeader(header) + claims(payload)
        // secret key = jwtKey(application) + alg
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(this.getSecretKey())
            .macAlgorithm(SecurityUtils.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
        
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrinciple(securityContext.getAuthentication()));
    }

    private static String extractPrinciple(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}
