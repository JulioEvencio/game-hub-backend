package julioigreja.gamehub.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JWTServiceImpl implements JWTService {

    private final String secretKey;
    private final Long validityInMilliseconds;

    private final String audienceURL;

    public JWTServiceImpl(@Value("${api.security.token.jwt.secret-key}") String secretKey, @Value("${api.security.token.jwt.expire-length}") Long validityInMilliseconds, @Value("${api.url.front-end}") String audienceURL) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;

        this.audienceURL = audienceURL;
    }

    @Override
    public String createAccessToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(validityInMilliseconds);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(validity))
                .withSubject(username)
                .withIssuer(this.getIssueURL())
                .withAudience(audienceURL)
                .sign(Algorithm.HMAC256(secretKey.getBytes()))
                .strip();
    }

    @Override
    public String createAccessTokenPasswordRecovery(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(900000L);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(validity))
                .withSubject(username)
                .withIssuer(this.getIssueURL())
                .withAudience(audienceURL)
                .sign(Algorithm.HMAC256(secretKey.getBytes()))
                .strip();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.getIssueURL())
                    .withAudience(audienceURL)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT.getExpiresAt().before(Date.from(Instant.now()))) {
                return false;
            }

            if (decodedJWT.getClaim("roles").isNull()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);

        return decodedJWT.getSubject();
    }

    private String getIssueURL() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

}
