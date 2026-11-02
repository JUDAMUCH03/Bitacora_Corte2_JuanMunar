package co.edu.eci.dosw.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantApplication {

    private static final String[] ENV_KEYS_TO_SANITIZE = {
        "DB_HOST", "DB_PORT", "DB_NAME", "DB_USER", "DB_PASSWORD",
        "SPRING_DATASOURCE_URL", "SPRING_DATASOURCE_USERNAME", "SPRING_DATASOURCE_PASSWORD",
        "JWT_SECRET", "SERVER_PORT"
    };

    public static void main(String[] args) {
        sanitizeEnvironmentProperties();
        SpringApplication.run(RestaurantApplication.class, args);
    }

    private static void sanitizeEnvironmentProperties() {
        for (String key : ENV_KEYS_TO_SANITIZE) {
            String value = System.getenv(key);
            if (value != null) {
                String sanitized = value.trim();
                if (!sanitized.isEmpty()) {
                    System.setProperty(key, sanitized);
                }
            }
        }
    }
}
