package com.api.security.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
/*
 * This class provide the security configuration for the application and is used to configure the security filter chain
 * the filter chain is a chain of filters that are applied to the incoming requests who execute the security logic in the order they are added
 * The security filter chain is created by the SecurityFilterChain bean
 * */
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /*
     * @return SecurityFilterChain
     * @param HttpSecurity http
     * @throws Exception
     * Set the http security configuration (security ignore routes, csrf, session management, authentication provider, jwtAuthFilter)
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable) //Disable csrf
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated()) //Allow all requests to /token/** and authenticate all other requests
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Disable session creation
                .authenticationProvider(authenticationProvider) //Add the authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //Add the jwtAuthFilter before the UsernamePasswordAuthenticationFilter
                .build(); //Build the security filter chain

    }
}
