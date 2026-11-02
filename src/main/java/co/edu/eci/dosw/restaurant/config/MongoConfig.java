package co.edu.eci.dosw.restaurant.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de resiliencia y timeouts para el cliente de MongoDB.
 * Limita el timeout de selección de servidor y conexión a 2 segundos
 * para evitar bloqueos del hilo HTTP ante caídas del clúster NoSQL.
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
        return clientSettingsBuilder -> {
            clientSettingsBuilder
                    .applyToClusterSettings(cluster ->
                            cluster.serverSelectionTimeout(2000, TimeUnit.MILLISECONDS))
                    .applyToSocketSettings(socket -> {
                        socket.connectTimeout(2000, TimeUnit.MILLISECONDS);
                        socket.readTimeout(2000, TimeUnit.MILLISECONDS);
                    });
        };
    }
}
