package com.mesaverde.service;

import com.mesaverde.dto.CustomUserDetails;
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
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Usuario user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado " + username));
        if(user.getEstado() == 0) {
            throw new DisabledException("Usuario deshabilitado");
        }else {
            return new CustomUserDetails(user);
        }
    }

    @Bean
    CommandLineRunner initUser() {
        return args -> {
            if (userRepository.findByUsername("pepita").isEmpty()) {
                Usuario user = new Usuario();
                user.setUsername("pepita");
                user.setEmail("pepita@gmail.com");
                user.setPassword(passwordEncoder.encode("secretpe12"));
                user.setEstado((short) 1);
                user.setNombre(user.getUsername());
                Rol roleUser = new Rol();
                roleUser.setId(1);
                roleUser.setName("ADMIN");
                user.setRoles(Set.of(roleUser));

                userRepository.save(user);
            }
        };
    }

}