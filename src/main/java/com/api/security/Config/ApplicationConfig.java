package com.api.security.Config;

import com.api.security.Entities.User;
import com.api.security.Repositories.UserRepository;
import com.api.security.Service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * This class provide the userDetailsService and the authenticationProvider to the application
 * The userDetailsService find the user by email or throw an exception if the user is not found
 * The authenticationProvider use the userDetailsService to find the user and set in the provider and set the passwordEncoder it´s a BCryptPasswordEncoder
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository repository;

    /*
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public User loadUserByUsername(String username) throws UsernameNotFoundException {
                return repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    /*
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(username -> userDetailsService().loadUserByUsername(username));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /*
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //1:38

    /*
     * @param authenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception
     * This method is used to create the authenticationManager, this is used to authenticate the user in the application is used in the AuthService
     * These method "override" the authenticationManager in the WebSecurityConfigurerAdapter, and for that reason I have access to the database in the authenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
