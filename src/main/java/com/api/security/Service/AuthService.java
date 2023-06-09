package com.api.security.Service;

import com.api.security.Config.JwtService;
import com.api.security.DTO.AuthenticationResponse;
import com.api.security.DTO.UserAuthRequest;
import com.api.security.DTO.UserRegisterRequest;
import com.api.security.Entities.User;
import com.api.security.Entities.UserRole;
import com.api.security.Exceptions.BadCredentials;
import com.api.security.Exceptions.InternalServerError;
import com.api.security.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRegisterRequest userRegisterRequest) {

        try {
            //builder pattern
            User user = User.builder().email(userRegisterRequest.getEmail()).password(passwordEncoder.encode(userRegisterRequest.getPassword())).role(UserRole.USER).firstname(userRegisterRequest.getFirstname()).lastname(userRegisterRequest.getLastname()).build();
            userRepository.save(user);
            //generate JWT
            String token = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(token).build();

        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }

    }


    public AuthenticationResponse authenticate(UserAuthRequest userRegisterRequest) { //is like login
        try {
            //the method authenticate is from AuthenticationManager and these use the AuthenticationProvider to authenticate the user who is configured in the applicationConfig
            //the AuthenticationProvider use the UserDetailsService to find the user and set in the provider and set the passwordEncoder it´s a BCryptPasswordEncoder
            //that is the reason why authenticate can compare the password of the user with the password in the database, a lot of beans are used to do this.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRegisterRequest.getEmail(), userRegisterRequest.getPassword()));
            User user = userRepository.findByEmail(userRegisterRequest.getEmail()).orElse(null);
            String token = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(token).build();

        } catch (RuntimeException e) {
            throw new BadCredentials("Bad credentials");
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void isValidEmail(String email) {
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        if (!email.matches(emailRegex)) {
            System.out.println("Invalid email");
            throw new BadCredentials("Invalid email");
        }


    }
}
