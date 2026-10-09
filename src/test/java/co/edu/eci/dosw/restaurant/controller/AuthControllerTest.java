package co.edu.eci.dosw.restaurant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.eci.dosw.restaurant.dto.request.LoginRequestDTO;
import co.edu.eci.dosw.restaurant.dto.response.TokenResponseDTO;
import co.edu.eci.dosw.restaurant.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("login - credenciales válidas retorna 200 OK con token JWT")
    void login_credencialesValidas_retornaTokenResponse() {
        LoginRequestDTO request = new LoginRequestDTO("admin@bluevelvet.com", "admin123");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin@bluevelvet.com");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_GERENTE"))).when(auth).getAuthorities();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtUtil.generarToken(eq("admin@bluevelvet.com"), any())).thenReturn("mocked.jwt.token");

        ResponseEntity<TokenResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mocked.jwt.token", response.getBody().getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("login - credenciales erróneas propaga BadCredentialsException")
    void login_credencialesInvalidas_lanzaBadCredentialsException() {
        LoginRequestDTO request = new LoginRequestDTO("admin@bluevelvet.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
        verify(jwtUtil, never()).generarToken(any(), any());
    }
}
