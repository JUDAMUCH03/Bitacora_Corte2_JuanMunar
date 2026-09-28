package co.edu.eci.dosw.restaurant.security;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "VGVzdFNlY3JldEtleVRlc3RTZWNyZXRLZXlUZXN0U2VjMTIzNDU2Nzg=";
    private final long expiration = 3600000; // 1 hora

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, expiration);
    }

    @Test
    @DisplayName("generarToken y extraerEmail - genera token y extrae el subject correctamente")
    void generarToken_extraerEmail_retornaEmailCorrecto() {
        String token = jwtUtil.generarToken("admin@bluevelvet.com", List.of("ROLE_GERENTE"));

        assertNotNull(token);
        assertFalse(token.isBlank());

        String email = jwtUtil.extraerEmail(token);
        assertEquals("admin@bluevelvet.com", email);
    }

    @Test
    @DisplayName("esValido - retorna true para un token recién generado")
    void esValido_tokenValido_retornaTrue() {
        String token = jwtUtil.generarToken("bar@bluevelvet.com", List.of("ROLE_BARTENDER"));

        assertTrue(jwtUtil.esValido(token));
    }

    @Test
    @DisplayName("esValido - retorna false para un token corrupto o manipulado")
    void esValido_tokenInvalido_retornaFalse() {
        String token = jwtUtil.generarToken("bar@bluevelvet.com", List.of("ROLE_BARTENDER"));
        String tokenAlterado = token + "xyz";

        assertFalse(jwtUtil.esValido(tokenAlterado));
        assertFalse(jwtUtil.esValido("invalid.token.structure"));
        assertFalse(jwtUtil.esValido(null));
    }

    @Test
    @DisplayName("esValido - retorna false para un token expirado")
    void esValido_tokenExpirado_retornaFalse() {
        // JwtUtil con tiempo de expiración negativo para simular token caducado
        JwtUtil expiredJwtUtil = new JwtUtil(secret, -1000);
        String token = expiredJwtUtil.generarToken("admin@bluevelvet.com", List.of("ROLE_GERENTE"));

        assertFalse(jwtUtil.esValido(token));
    }
}
