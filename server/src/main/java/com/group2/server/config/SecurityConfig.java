package com.group2.server.config;


import com.group2.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetails;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()  //allow access to h2 console
                        .requestMatchers("/api/dogs/available").permitAll() //publicly available dogs
                        .requestMatchers("/api/dogs", "/api/dogs/{id}").permitAll() //public access to view dogs
                        .requestMatchers("/api/requests", "/api/requests/{id}").hasAuthority("ADMIN")
                        .requestMatchers("/api/dogs", "/api/dogs/{id}").hasAuthority("ADMIN") //Admin Only
                        .requestMatchers("/api/requests", "/api/requests/{id}/status").hasAuthority("ADMIN")
                        .requestMatchers("/api/requests").authenticated() //user to make adoption request
                        .requestMatchers("/api/users", "/api/users/{id}").hasAuthority("ADMIN") // user endpoints and secured for admin
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .httpBasic(basic -> {}); //use basic authentication for simplicity

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return (UserDetails) userService.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            }
        };
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder); // passwordencoder
        return authenticationManagerBuilder.build();
    }

}