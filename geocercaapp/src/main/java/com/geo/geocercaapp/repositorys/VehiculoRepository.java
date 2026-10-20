package com.geo.geocercaapp.repositorys;

import com.geo.geocercaapp.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Long> {
    List<Vehiculo> findByEmpresaIdAndActivoTrue(Long empresaId);
    Optional<Vehiculo> findByIdAndEmpresaId(Long id,Long empresaId);
    Optional<Vehiculo> findByConductorIdAndActivoTrue(Long conductorId);
    boolean existsByPlaca(String placa);
    boolean existsByPlacaAndIdNot(String placa, Long id);
}
