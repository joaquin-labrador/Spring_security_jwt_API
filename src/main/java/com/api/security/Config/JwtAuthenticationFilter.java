package com.api.security.Config;

import com.api.security.Entities.User;
import com.api.security.Service.JwtService;
import com.api.security.Service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /*
     * @params HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
     * @throws ServletException, IOException
     * This method is called by the doFilter method in the SecurityFilterChain
     * This method is used to extract the jwt token from the request header and authenticate the user
     * */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        //verify that the authorization header is not null and starts with "Bearer "
        if (authorizationHeader == null || authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //Continue the filter chain, similar to next() in express
            return;
        }
        final int indexStartJwtToken = 7; //The index of the first character of the jwt token in the authorization header
        jwt = authorizationHeader.substring(indexStartJwtToken); //Extract the jwt token from the authorization header
        userEmail = jwtService.extractUsername(jwt); //Extract the username from the jwt token
        //verify that the user email is not null and that the user is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = userDetailsService.loadUserByUsername(userEmail); //Get the user details from the database
            //verify that the jwt token is valid
            if (jwtService.isValidUsernameToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //Create the authToken
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Set the details of the authToken of the request header(like the ip address)
                SecurityContextHolder.getContext().setAuthentication(authToken); //Set the authToken in the context of Spring security
            }
            filterChain.doFilter(request, response); //Continue the filter chain, similar to next() in express
        }

    }

}
