package com.geo.geocercaapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "vehiculos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(nullable = false)
    private String placa;

    private String marca;

    private String modelo;

    @Column(name = "condutor_id")
    private Long condutorId;

    @Column(name = "costo_por_standby")
    @Builder.Default
    private Double costoPorStandby = 50000.0;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(nullable = false, updatable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = Instant.now();
    }

}
