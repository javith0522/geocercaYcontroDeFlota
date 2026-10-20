package com.geo.geocercaapp.entity;

import com.geo.geocercaapp.enums.TipoSitio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "sitios")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Sitio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false)
    private double latitud;
    @Column(nullable = false)
    private double longitud;

    @Column(nullable = false)
    @Builder.Default
    private Integer radioMetros = 100;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSitio tipo;

    @Column(nullable = false)
    private Integer slaMinutos;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo =  true;

    @Column(name = "costo_penalidad_hora")
    @Builder.Default
    private Double costoPenalidadHora = 60000.0;

    @Column(name = "tiempo_estimado_descargue_minutos")
    @Builder.Default
    private Integer tiempoEstimadoDescargueMinutos = 45;

    @Column(nullable = false, updatable = false)
    private Instant creadoEn;

    @PrePersist
    public void onCreate()
    {
        this.creadoEn = Instant.now();
    }


}
