package co.edu.eci.dosw.restaurant.security;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.persistence.entity.UsuarioEntity;
import co.edu.eci.dosw.restaurant.repository.UsuarioRepository;

@Service("usuarioDetailsService")
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UsuarioDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando detalles de seguridad para usuario: {}", email);
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        boolean enabled = usuario.getActivo() == null || Boolean.TRUE.equals(usuario.getActivo());
        String rol = usuario.getRol();
        if (rol != null && !rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                enabled,
                true,
                true,
                true,
                rol != null ? List.of(new SimpleGrantedAuthority(rol)) : List.of()
        );
    }
}
