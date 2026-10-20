package com.geo.geocercaapp.DTOS;

import com.geo.geocercaapp.enums.TipoSitio;

public record ActualizarSitioRequest(
        String nombre,
        String direccion,
        Double latitud,
        Double longitud,
        Integer rarioMetros,
        TipoSitio tipo,
        Integer slMintutos,
        Boolean activo
) {
}
