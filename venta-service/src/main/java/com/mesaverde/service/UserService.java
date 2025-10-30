package com.mesaverde.service;

import com.mesaverde.entity.Rol;
import com.mesaverde.entity.Usuario;
import com.mesaverde.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
 

   
    public Usuario obtenerUsuario(String username) {
        // TODO Auto-generated method stub
        Usuario user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado " + username));
       return user;
    }

   
}