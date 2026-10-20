package com.geo.geocercaapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.time.Instant;

@Entity
@Table(name = "usuarios")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String celular;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column( name = "empresa_id")
    private Long empresaId;

    @Column(name = "sitio_id")
    private Long sitioId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    public Instant creadoEn;

    @PrePersist
    public void onCreate()
    {
        this.creadoEn = Instant.now();
    }
}
