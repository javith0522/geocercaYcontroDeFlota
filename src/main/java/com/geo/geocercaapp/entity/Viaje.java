package com.geo.geocercaapp.entity;

import com.geo.geocercaapp.enums.EstadoViaje;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "viajes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "conductor_id", nullable = false)
    private Long conductorId;

    @Column(name = "sitio_origen_id", nullable = false)
    private Long sitioOrigenId;

    @Column(name = "sitio_destino_id", nullable = false)
    private Long sitioDestinoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoViaje estado = EstadoViaje.CREADO;

    private Instant horaLllegadaSitio;
    private Instant horaInicioAtencion;
    private Instant horaFinAtencion;
    private Instant horaCierre;

    private Instant primeraDeteccionSitio;

    @Column(nullable = false, updatable = false)
    private Instant creaEn;

    @PrePersist
    protected void onCreate() {
        this.creaEn = Instant.now();
    }
}
