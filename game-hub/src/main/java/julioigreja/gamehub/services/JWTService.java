package julioigreja.gamehub.services;

import java.util.List;

public interface JWTService {

    String createAccessToken(String username, List<String> roles);

    boolean validateToken(String token);

    String getUsername(String token);

}
