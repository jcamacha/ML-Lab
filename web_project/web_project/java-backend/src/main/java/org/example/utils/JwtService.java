package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.example.entities.User;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "ML_LAB_SUPER_SECRET_KEY_FOR_JWT_AUTHENTICATION_ESCOM_IPN";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getUserId())
                .withClaim("role", user.getRole())
                .withClaim("name", user.getName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public DecodedJWT verifyToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        // Si el token incluye la palabra "Bearer ", limpiarla
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    public String getEmailFromToken(DecodedJWT jwt) {
        return jwt != null ? jwt.getSubject() : null;
    }

    public Long getUserIdFromToken(DecodedJWT jwt) {
        return jwt != null ? jwt.getClaim("userId").asLong() : null;
    }

    public String getRoleFromToken(DecodedJWT jwt) {
        return jwt != null ? jwt.getClaim("role").asString() : null;
    }
}
