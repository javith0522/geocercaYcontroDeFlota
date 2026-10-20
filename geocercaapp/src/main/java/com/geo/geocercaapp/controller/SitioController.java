package com.geo.geocercaapp.controller;

import com.geo.geocercaapp.DTOS.CrearSItioRequest;
import com.geo.geocercaapp.DTOS.SitioResponse;
import com.geo.geocercaapp.service.SitioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sitios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN_TRANSPORTADORA')")
public class SitioController {

    private final SitioService sitioService;

    @GetMapping
    public List<SitioResponse> listar(){
        return sitioService.listar().stream()
                .map(SitioResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public SitioResponse obtener(@PathVariable Long id){
        return  SitioResponse.from(sitioService.buscarSitioPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SitioResponse crear(@Valid @RequestBody CrearSItioRequest request){
        return SitioResponse.from(sitioService.crear(request));
    }

}
