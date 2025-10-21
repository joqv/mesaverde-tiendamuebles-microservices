package com.bankito.dto;

import com.mesaverde.entity.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User{

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String email;

    public CustomUserDetails(Usuario user) {
        super(user.getUsername(), user.getPassword(), user.getRoles().stream().map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getName())).toList());
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

}