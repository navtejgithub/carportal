package com.carportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
        .csrf(AbstractHttpConfigurer::disable); // Disable CSRF
//                .cors(cors->cors.disable()); // Disable cors

//        http.authorizeHttpRequests(
//                req->{
//                    req.anyRequest().permitAll();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                .requestMatchers("/api/v1/auth/login").permitAll()
                        // if you want signup to require JWT:
                .requestMatchers("/api/v1/auth/signup").authenticated()
                        // this is optional, because anyRequest().authenticated()
                        // will already cover /api/v1/car/add
                .requestMatchers("/api/v1/car/add").authenticated()
                        // must be LAST
                        .anyRequest()
                                  .authenticated()
                );

//        http.authorizeHttpRequests(
//                authorize -> authorize
//                        .anyRequest().permitAll()
////                        .anyRequest().authenticated()
////                        .requestMatchers("/api/v1/auth")
//        );
        return http.build();
//        return http.build();




    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

