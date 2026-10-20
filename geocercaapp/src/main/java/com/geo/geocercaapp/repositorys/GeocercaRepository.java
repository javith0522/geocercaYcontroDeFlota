package com.geo.geocercaapp.repositorys;

import com.geo.geocercaapp.entity.Geocerca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeocercaRepository extends JpaRepository<Geocerca, Long> {
    List<Geocerca> findByViajeidOrderByTimestampAsc(String sitio);
}
