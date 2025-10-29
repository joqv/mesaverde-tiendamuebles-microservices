package com.mesaverde.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //http
        //        .csrf(csrf -> csrf.disable())
         //       .authorizeHttpRequests(auth -> auth
         //               // puedes abrir los endpoints públicos si quieres
        //                .requestMatchers("/actuator/**").permitAll()
        //                .anyRequest().authenticated()
        //        )
                // 🟢 Esto hace que el servicio lea y valide el Bearer token
        //        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        //return http.build();

        return http
                .authorizeHttpRequests(auth -> auth
                       .anyRequest().permitAll()
               )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    //@Bean
    //public JwtDecoder jwtDecoder() {
        // ⚠️ Cambia esta URL por la pública de tu auth-service
    //    return NimbusJwtDecoder.withJwkSetUri("http://localhost:9000/oauth2/jwks").build();
    //}
}

