package com.geo.geocercaapp.entity;

import com.geo.geocercaapp.enums.TipoEventoGeocerca;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "goecerca")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Geocerca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @Column(name = "sitio_id", nullable = false)
    private Long sitioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEventoGeocerca tipo;

    @Column(nullable = false)
    private Double latitud;
    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private Instant timestamp;

}
