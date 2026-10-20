package com.geo.geocercaapp.entity;

import com.geo.geocercaapp.enums.EstadoIncidente;
import com.geo.geocercaapp.enums.OrigenIncidente;
import com.geo.geocercaapp.enums.TipoIncidente;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "incidentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "viaje_id")
    private Long viajeId;

    @Column(name = "reportado_por")
    private Long reportadoPor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenIncidente incidente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoIncidente tipo;

    @Column(length = 1000)
    private String descripcion;

    private String evidenciaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoIncidente estado = EstadoIncidente.ABIERTO;

    @Column(nullable = false, updatable = false)
    private Instant created;

    private  Instant resueltoEn;

    @PrePersist
    public void onCreate()
    {
        this.created = Instant.now();
    }
}
