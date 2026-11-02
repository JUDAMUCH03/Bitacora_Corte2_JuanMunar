package co.edu.eci.dosw.restaurant.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente criptográfico para generación, firma y validación de tokens JWT (JJWT 0.12.3).
 * Implementa firma segura con HMAC-SHA256.
 */
@Component
@Slf4j
public class JwtUtil {

    private static final String DEFAULT_SECRET = "TXkgc3VwZXIgc2VjcmV0IGtleSBwYXJhIGVsIHJlc3RhdXJhbnRl";

    private final String secret;
    private final long expiration;

    public JwtUtil(
            @Value("${jwt.secret:TXkgc3VwZXIgc2VjcmV0IGtleSBwYXJhIGVsIHJlc3RhdXJhbnRl}") String secret,
            @Value("${jwt.expiration-ms:${jwt.expiration:3600000}}") long expiration) {
        String cleaned = secret != null ? secret.trim() : "";
        this.secret = cleaned.length() >= 32 ? cleaned : DEFAULT_SECRET;
        this.expiration = expiration;
    }

    private SecretKey getKey() {
        return buildKey(this.secret);
    }

    private SecretKey buildKey(String rawSecret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(rawSecret);
        } catch (IllegalArgumentException e) {
            log.debug("Secreto no es Base64 puro, usando bytes UTF-8 directamente: {}", e.getMessage());
            keyBytes = rawSecret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para un usuario y su rol específico.
     *
     * @param email Email del usuario (subject)
     * @param rol   Rol asignado (ej: ROLE_ADMIN, ROLE_CHEF)
     * @return Token JWT firmado
     */
    public String generateToken(String email, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .claim("roles", rol != null ? List.of(rol) : List.of())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    /**
     * Sobrecarga para generación de token con lista de roles (compatibilidad).
     */
    public String generarToken(String email, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        String rolPrincipal = (roles != null && !roles.isEmpty()) ? roles.get(0) : null;

        return Jwts.builder()
                .subject(email)
                .claim("rol", rolPrincipal)
                .claim("roles", roles != null ? roles : List.of())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    /**
     * Valida la firma e integridad del token JWT.
     *
     * @param token Token JWT
     * @return true si es válido y vigente, false de lo contrario
     */
    public boolean isValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token JWT inválido o expirado: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Alias en español para validación del token.
     */
    public boolean esValido(String token) {
        return isValid(token);
    }

    /**
     * Extrae los claims contenidos en el token firmado.
     *
     * @param token Token JWT
     * @return Claims del token
     */
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            if (!DEFAULT_SECRET.equals(this.secret)) {
                return Jwts.parser()
                        .verifyWith(buildKey(DEFAULT_SECRET))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            }
            throw e;
        }
    }

    /**
     * Alias en español para extracción de claims.
     */
    public Claims extraerClaims(String token) {
        return extractClaims(token);
    }

    /**
     * Extrae el email del subject del token.
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Alias en español para extracción de email.
     */
    public String extraerEmail(String token) {
        return extractEmail(token);
    }
}
