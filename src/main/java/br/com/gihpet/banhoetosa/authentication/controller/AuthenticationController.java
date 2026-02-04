package br.com.gihpet.banhoetosa.authentication.controller;

import br.com.gihpet.banhoetosa.authentication.dto.UserLoginRequestDTO;
import br.com.gihpet.banhoetosa.authentication.service.AuthenticationService;
import br.com.gihpet.banhoetosa.authentication.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(AuthenticationService authenticationService, TokenBlacklistService tokenBlacklistService) {
        this.authenticationService = authenticationService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Performs the user login!")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok().body(authenticationService.login(loginRequestDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidates the current JWT token")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        tokenBlacklistService.addToBlacklist(token);
        return ResponseEntity.ok().build();
    }
}
