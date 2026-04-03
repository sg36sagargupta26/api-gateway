package com.lld.api_gateway.configuration;

import com.lld.api_gateway.component.AdminTokenAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injected from the previous setup (admin token filter)
    private final AdminTokenAuthFilter adminTokenAuthFilter;

    public SecurityConfig(AdminTokenAuthFilter adminTokenAuthFilter) {
        this.adminTokenAuthFilter = adminTokenAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/token").permitAll()   // public - issue token
                        .requestMatchers("/hello").authenticated()    // requires valid JWT
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin token filter handles this
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())               // validates Bearer JWT on /hello
                )
                .addFilterBefore(adminTokenAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}