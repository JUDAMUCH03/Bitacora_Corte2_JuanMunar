package co.edu.eci.dosw.restaurant.security;

import co.edu.eci.dosw.restaurant.persistence.entity.UsuarioEntity;
import co.edu.eci.dosw.restaurant.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando detalles de seguridad para usuario: {}", email);
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        boolean enabled = Boolean.TRUE.equals(usuario.getActivo());

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                enabled,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(usuario.getRol()))
        );
    }
}
