package com.geo.geocercaapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
public class AuthContex {

    public UsuarioPrincipal usuarioActual(){
        return (UsuarioPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long emppresaIdActual(){
        return usuarioActual().empresaId();
    }

    public Long sitioIdActual(){
        return usuarioActual().sitioId();
    }
}
