package com.ContentAPI.ContentAPI.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/user/current").permitAll()

                        .requestMatchers(HttpMethod.GET, "/content").permitAll()
                        .requestMatchers(HttpMethod.POST, "/content/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/content/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/content/admin").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/rating/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/rating/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/rating/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/rating/admin").hasRole("ADMIN")

                        .requestMatchers(getOpenedResources()).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private String[] getOpenedResources() {
        return new String[]{
                "/swagger-ui/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/v3/api-docs",
                "/v3/api-docs/**"
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}