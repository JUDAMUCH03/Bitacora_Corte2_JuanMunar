package co.edu.eci.dosw.restaurant.security;

import co.edu.eci.dosw.restaurant.persistence.entity.UsuarioEntity;
import co.edu.eci.dosw.restaurant.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("loadUserByUsername - usuario existente retorna UserDetails con authorities")
    void loadUserByUsername_usuarioExiste_retornaUserDetails() {
        UsuarioEntity entity = UsuarioEntity.builder()
                .id(1L)
                .email("admin@bluevelvet.com")
                .password("$2a$10$encryptedPassword")
                .rol("ROLE_GERENTE")
                .activo(true)
                .build();

        when(usuarioRepository.findByEmail("admin@bluevelvet.com")).thenReturn(Optional.of(entity));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@bluevelvet.com");

        assertNotNull(userDetails);
        assertEquals("admin@bluevelvet.com", userDetails.getUsername());
        assertEquals("$2a$10$encryptedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GERENTE")));
    }

    @Test
    @DisplayName("loadUserByUsername - usuario no encontrado lanza UsernameNotFoundException")
    void loadUserByUsername_noExiste_lanzaUsernameNotFoundException() {
        when(usuarioRepository.findByEmail("desconocido@bluevelvet.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("desconocido@bluevelvet.com")
        );
    }
}
