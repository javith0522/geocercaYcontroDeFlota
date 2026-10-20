package com.geo.geocercaapp.DTOS;

import com.geo.geocercaapp.entity.Sitio;
import com.geo.geocercaapp.enums.TipoSitio;

public record SitioResponse (
        Long id,
        String nombre,
        String direccion,
        Double latitud,
        Double longitud,
        Integer radioMetros,
        TipoSitio tipo,
        Integer slaMinutos,
        Integer tiempoEstimadoDeCarga,
        Double costoPenalPorHora,
        boolean activo
){
    public static SitioResponse from(Sitio s)
    {
        return new SitioResponse(
                s.getId(),
                s.getNombre(),
                s.getDireccion(),
                s.getLatitud(),
                s.getLongitud(),
                s.getRadioMetros(),
                s.getTipo(),
                s.getSlaMinutos(),
                s.getTiempoEstimadoDescargueMinutos(),
                s.getCostoPenalidadHora(),
                s.isActivo()
        );
    }
}
