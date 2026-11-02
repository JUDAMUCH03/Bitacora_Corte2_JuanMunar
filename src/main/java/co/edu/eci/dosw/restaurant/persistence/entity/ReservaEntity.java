package co.edu.eci.dosw.restaurant.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import co.edu.eci.dosw.restaurant.model.domain.enums.EstadoReserva;

/**
 * Reserva de mesa persistida en la tabla relacional {@code reservas}.
 */
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_cliente", nullable = false, length = 120)
    private String nombreCliente;

    @Column(name = "email_cliente", nullable = false, length = 150)
    private String emailCliente;

    @Column(name = "telefono", length = 40)
    private String telefono;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false, length = 5)
    private String hora;

    @Column(name = "numero_personas", nullable = false)
    private Integer numeroPersonas;

    @Column(name = "mesa_numero")
    private Integer mesaNumero;

    @Column(name = "zona", length = 60)
    private String zona;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(name = "notas", length = 500)
    private String notas;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
