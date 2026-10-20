package com.geo.geocercaapp.repositorys;

import com.geo.geocercaapp.entity.Sitio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SitioRepository extends JpaRepository<Sitio, Long> {
    List<Sitio> findByEmpresaIdAndActivoTrue(Long empresaId);

    Optional<Sitio> findByIdAndEmpresaId(Long id,  Long empresaId);


    @Query(value = """
         SELECT * FROM sitios s
         WHERE s.empresa_id = :empresaId
         AND s.activo = true
         AND (
                  6371000 * 2 * ASIN(
                  SQRT(
                        POWER(SIN(RADIANS(:lat - s.latitud) / 2), 2) +
                        COS(RADIANS(s.latitud)) * COS(RADIANS(:lat)) * 
                        POWER(SIN(RADIANS(:lng - s.longitud) / 2), 2)
                           )
                        )
                  ) <= s.radio_metros
                  LIMITED 1
                   """, nativeQuery = true)
    Optional<Sitio> encontrarSitioQueContienePunto(
            @Param("empresaId") Long empresaId,
            @Param("lat") Double lat,
            @Param("lng") Double lng
    );
}
