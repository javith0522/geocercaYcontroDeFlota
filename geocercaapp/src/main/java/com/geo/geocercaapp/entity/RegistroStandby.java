package com.geo.geocercaapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "registros_standby")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroStandby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @Column(name = "siti_id", nullable = false)
    private Long sitiId;

    @Column(nullable = false)
    private Instant horaLlegada;

    private Instant horaInicioAtencion;

    private Instant horaSalida;

    private Integer minutosStanBy;

    private String motivoDemora;

    @Column(nullable = false)
    @Builder.Default
    private Boolean cerrado = false;

    @Column(nullable = false, updatable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = Instant.now();
    }
}
