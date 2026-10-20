package com.geo.geocercaapp.security;

import com.geo.geocercaapp.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UsuarioPrincipal(
        Long id,
        String email,
        String contrasena,
        String rol,
        Long EmpresaId,
        Long sitio,
        boolean activo
) implements UserDetails {

    public static   UsuarioPrincipal from(Usuario usuario)
    {return new UsuarioPrincipal(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getRole().getRoleName(),
            usuario.getEmpresaId(),
            usuario.getSitioId(),
            usuario.getActivo()
    );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {   return contrasena;}
    @Override
    public String getUsername() {return email;}
    @Override
    public boolean isAccountNonExpired() {return true;}
}
