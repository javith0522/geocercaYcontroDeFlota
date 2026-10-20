package com.geo.geocercaapp.DTOS;
import com.geo.geocercaapp.enums.TipoSitio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearSItioRequest(
        @NotBlank String nombre,
        @NotBlank String direccion,
        @NotBlank double longitud,
        @NotBlank double latitud,
        @Positive Integer radioMetro,
        @NotNull TipoSitio tipo,
        Integer slMinutos

        ) {
}
