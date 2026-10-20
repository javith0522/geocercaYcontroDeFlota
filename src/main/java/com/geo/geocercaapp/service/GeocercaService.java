package com.geo.geocercaapp.service;

import com.geo.geocercaapp.entity.Geocerca;
import com.geo.geocercaapp.entity.RegistroStandby;
import com.geo.geocercaapp.entity.Sitio;
import com.geo.geocercaapp.entity.Viaje;
import com.geo.geocercaapp.enums.EstadoViaje;
import com.geo.geocercaapp.enums.TipoEventoGeocerca;
import com.geo.geocercaapp.repositorys.GeocercaRepository;
import com.geo.geocercaapp.repositorys.RegistroStandbyRepository;
import com.geo.geocercaapp.repositorys.SitioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GeocercaService {
    private final GeocercaRepository geocercaRepository;
    private final SitioRepository sitioRepository;
    private final RegistroStandbyRepository registroStandbyRepository;
    public record ResultadoProcesamiento(boolean dentroGeocerca, Viaje viaje, String mensaje){}

    @Value("${app.geocerca.tolerancia-permanencia-segundos}")
    private long toleranciaSegundos;

    @Value("${app.geocercca.precision-maxima-metros}")
    private double precisionMaximaMetros;

    @Transactional
    public ResultadoProcesamiento procesarUbicacion(
            Viaje viaje, Long empresaId, double lat, double lng, Float exactitud, Instant timestamp
    ){
        Instant momento = timestamp != null ? timestamp : Instant.now();

        if (exactitud != null && exactitud > precisionMaximaMetros) {
            return new ResultadoProcesamiento(false, viaje,
                    "Lectura GPS descartada por baja precision (%.0fm)".formatted(exactitud));
        }

        Optional<Sitio> sitioEncontrado = sitioRepository.encontrarSitioQueContienePunto(empresaId, lat, lng);

        boolean estaEnDestino = sitioEncontrado.isPresent()
                && sitioEncontrado.get().getId().equals(viaje.getSitioOrigenId());

        if (estaEnDestino) {
            return manejarDentroGeocerca(viaje, sitioEncontrado.get(), lat, lng, timestamp);
        }else {
            return  manejarFueraGeocerca(viaje, lat, lng, momento);
        }
    }

    private ResultadoProcesamiento manejarDentroGeocerca(
            Viaje viajeId, Sitio sitio, double lat, double lng, Instant momento
    ){
        if (viajeId.getEstado() == EstadoViaje.EN_SITIO || viajeId.getEstado() == EstadoViaje.EN_ATENCION){
            return new ResultadoProcesamiento(true, viajeId, "Vehiculo dentro del sitio");
        }

        if (viajeId.getEstado() != EstadoViaje.EN_RUTA){
            return new ResultadoProcesamiento(true, viajeId, "Vehiculo no esta en RUTA, se ignora el geocerca");
        }

        if (viajeId.getPrimeraDeteccionSitio() == null){
            viajeId.setPrimeraDeteccionSitio(momento);
            viajeSeriveces.guardar(viajeId);
            return new ResultadoProcesamiento(true, viajeId, "Entrada detectada, confirmado permanencia");
        }

        long segundosDentro = Duration.between(viajeId.getPrimeraDeteccionSitio(), momento).getSeconds();

        if (segundosDentro < toleranciaSegundos){
            return  new ResultadoProcesamiento(true, viajeId, "Confirmada permanencia en el sitio");
        }

        confirmarEntrada(viajeId, sitio, lat, lng, viajeId.getPrimeraDeteccionSitio());
        return new ResultadoProcesamiento(true, viajeId, "Entrada confirmada al sitiio" + sitio.getNombre());
    }

    private  ResultadoProcesamiento manejarFueraGeocerca(Viaje viaje, double lat, double lng, Instant momento){
        if (viaje.getEstado() == EstadoViaje.EN_RUTA && viaje.getPrimeraDeteccionSitio() != null){

            viaje.setPrimeraDeteccionSitio(null);
            viajeService.guardar(viaje);
            return new ResultadoProcesamiento(false, viaje, "Salida antes de confirmar entrada, se reinicia deteccion");
        }

        if (viaje.getEstado() == EstadoViaje.EN_SITIO || viaje.getEstado() == EstadoViaje.EN_ATENCION){
            confirmarSalida(viaje, viaje.getSitioDestinoId(), lat, lng, momento );
            return new ResultadoProcesamiento (false, viaje, "Salida de sitio confirmada");
        }

        return new ResultadoProcesamiento(false, viaje, "Vehiculo fuera de Geocerca");
    }

    public void confirmarEntrada(Viaje viaje, Sitio sitio, double lat, double lng, Instant horaLlegada){
        geocercaRepository.save(Geocerca.builder()
                        .viajeId(viaje.getId())
                        .sitioId(sitio.getId())
                        .tipo(TipoEventoGeocerca.ENTRADA)
                        .latitud(lat)
                        .longitud(lng)
                        .timestamp(horaLlegada)
                .build());

        registroStandbyRepository.save(RegistroStandby.builder()
                        .empresaId(viaje.getEmpresaId())
                        .viajeId(viaje.getId())
                        .sitiId(sitio.getId())
                        .horaLlegada(horaLlegada)
                        .cerrado(false)
                .build());

        viaje.setHoraLllegadaSitio(horaLlegada);
        viajeService.transicionar(viaje, EstadoViaje.EN_SITIO);
    }

    public void confirmarSalida(Viaje viaje, Long sitioId, double lat, double lng, Instant momento){
        geocercaRepository.save(Geocerca.builder()
                        .viajeId(viaje.getId())
                        .sitioId(sitioId)
                        .tipo(TipoEventoGeocerca.SALIDA)
                        .longitud(lat)
                        .latitud(lng)
                .timestamp(momento)
                .build());

        Sitio sitio = sitioRepository.findById(sitioId).orElse(null);

        registroStandbyRepository.findByViajeIdAndSitioIdAndCerradoFalse(viaje.getId(), String.valueOf(sitioId))
                .ifPresent(registo -> {
                    if (sitio != null) {

                    }
                } );
    }



}
