package com.api.security.Controller;

import com.api.security.DTO.AuthenticationResponse;
import com.api.security.DTO.UserAuthRequest;
import com.api.security.DTO.UserRegisterRequest;
import com.api.security.Config.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ResponseEntity.ok(authService.register(userRegisterRequest));
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserAuthRequest userRegisterRequest) {
        return ResponseEntity.ok(authService.authenticate(userRegisterRequest));
    }
}
