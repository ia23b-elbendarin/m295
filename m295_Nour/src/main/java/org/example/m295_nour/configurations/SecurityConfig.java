package org.example.m295_nour.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger: nur nach Login sichtbar (USER oder ADMIN)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").hasAnyRole("USER", "ADMIN")

                        // --- Rezepte ---
                        .requestMatchers(HttpMethod.GET, "/api/rezepte/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/rezepte/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/rezepte/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rezepte/**").hasRole("ADMIN")

                        // --- Zutaten ---
                        .requestMatchers(HttpMethod.GET, "/api/zutaten/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/zutaten/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/zutaten/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/zutaten/**").hasRole("ADMIN")

                        // Alles andere NUR nach Login erlaubt:
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}password")  // wichtig: {noop} für Klartextpasswort
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
