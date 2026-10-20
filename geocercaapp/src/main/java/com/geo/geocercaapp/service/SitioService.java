package com.geo.geocercaapp.service;

import com.geo.geocercaapp.DTOS.ActualizarSitioRequest;
import com.geo.geocercaapp.DTOS.CrearSItioRequest;
import com.geo.geocercaapp.entity.Sitio;
import com.geo.geocercaapp.exception.RecursosNoEncontrados;
import com.geo.geocercaapp.repositorys.SitioRepository;
import com.geo.geocercaapp.security.AuthContex;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SitioService {

    private final SitioRepository sitioRepository;
    private final AuthContex authContex;

    public List<Sitio> listar() {
        return sitioRepository.findByEmpresaIdAndActivoTrue(authContex.emppresaIdActual());
    }

    public Sitio buscarSitioPorId(Long id){
        return sitioRepository.findByIdAndEmpresaId(id, authContex.emppresaIdActual())
                .orElseThrow(() -> new RecursosNoEncontrados("Sitio no encontrado" + id));
    }

    @Transactional
    public Sitio crear(CrearSItioRequest request){
        Sitio sitio = Sitio.builder()
                .empresaId(authContex.emppresaIdActual())
                .direccion(request.direccion())
                .nombre(request.nombre())
                .latitud(request.latitud())
                .longitud(request.longitud())
                .radioMetros(request.radioMetro())
                .tipo(request.tipo())
                .slaMinutos(request.slMinutos())
                .activo(true)
                .build();
        return sitioRepository.save(sitio);
    }

    @Transactional
    public Sitio actualizarSitio(Long id, ActualizarSitioRequest request){
        Sitio sitio = buscarSitioPorId(id);

        if (request.nombre() != null) sitio.setNombre(request.nombre());
        if (request.direccion() != null) sitio.setDireccion(request.direccion());
        if (request.latitud() != null) sitio.setLatitud(request.latitud());
        if (request.longitud() != null) sitio.setLongitud(request.longitud());
        if (request.tipo() != null) sitio.setTipo(request.tipo());
        if (request.rarioMetros() != null) sitio.setRadioMetros(request.rarioMetros());
        if (request.activo() != null) sitio.setActivo(request.activo());

        return  sitioRepository.save(sitio);
    }

    @Transactional
    public void eliminarSitio(Long id){
        Sitio sitio = buscarSitioPorId(id);
        sitio.setActivo(false);
        sitioRepository.save(sitio);
    }

}
