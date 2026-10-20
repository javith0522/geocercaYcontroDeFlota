package com.geo.geocercaapp.repositorys;

import com.geo.geocercaapp.entity.RegistroStandby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistroStandbyRepository extends JpaRepository<RegistroStandby,Long> {
    Optional<RegistroStandby> findByViajeIdAndSitioIdAndCerradoFalse(Long viajeId, String sitio);
    List<RegistroStandby> findByEmpresaIdOrderByCreadoEnDesc(Long viajeId);
    List<RegistroStandby> findBySitioOrderByCreadoEnDesc(String sitio);
}
