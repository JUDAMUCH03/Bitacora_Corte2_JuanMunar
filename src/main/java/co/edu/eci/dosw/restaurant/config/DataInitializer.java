package co.edu.eci.dosw.restaurant.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import co.edu.eci.dosw.restaurant.persistence.entity.UsuarioEntity;
import co.edu.eci.dosw.restaurant.repository.UsuarioRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            log.info("Inicializando usuarios por defecto en PostgreSQL (Blue Velvet)...");

            UsuarioEntity admin = UsuarioEntity.builder()
                    .email("admin@bluevelvet.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol("ROLE_ADMIN")
                    .activo(true)
                    .build();

            UsuarioEntity chef = UsuarioEntity.builder()
                    .email("chef@bluevelvet.com")
                    .password(passwordEncoder.encode("chef123"))
                    .rol("ROLE_CHEF")
                    .activo(true)
                    .build();

            UsuarioEntity cliente = UsuarioEntity.builder()
                    .email("cliente@bluevelvet.com")
                    .password(passwordEncoder.encode("cliente123"))
                    .rol("ROLE_CLIENTE")
                    .activo(true)
                    .build();

            UsuarioEntity mesero = UsuarioEntity.builder()
                    .email("mesero@bluevelvet.com")
                    .password(passwordEncoder.encode("mesero123"))
                    .rol("ROLE_MESERO")
                    .activo(true)
                    .build();

            UsuarioEntity bartender = UsuarioEntity.builder()
                    .email("bar@bluevelvet.com")
                    .password(passwordEncoder.encode("bar123"))
                    .rol("ROLE_BARTENDER")
                    .activo(true)
                    .build();

            usuarioRepository.saveAll(List.of(admin, chef, cliente, mesero, bartender));
            log.info("Usuarios iniciales creados exitosamente.");
        } else {
            log.info("Base de datos de usuarios ya contiene registros. Omitiendo inicialización.");
        }
    }
}
