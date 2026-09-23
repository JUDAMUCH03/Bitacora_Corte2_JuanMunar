package co.edu.eci.dosw.restaurant.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "platos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // la BD asigna el ID
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double precio;

    @Column(nullable = false, length = 60)
    private String categoria;

    private String descripcion;

    @Column(nullable = false)
    private Boolean disponible;

    @CreationTimestamp  // Hibernate la llena automáticamente
    private LocalDateTime creadoEn;
}