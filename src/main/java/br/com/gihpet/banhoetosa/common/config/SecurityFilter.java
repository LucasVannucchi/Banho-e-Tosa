package br.com.gihpet.banhoetosa.common.config;

import br.com.gihpet.banhoetosa.authentication.service.TokenBlacklistService;
import br.com.gihpet.banhoetosa.authentication.service.TokenService;
import br.com.gihpet.banhoetosa.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, TokenBlacklistService tokenBlacklistService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = recoverToken(request);

        if (token != null) {
            if (tokenBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            var login = tokenService.tokenValidation(token);
            if (login != null) {
                UserDetails userDetails = (UserDetails) userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var auth = request.getHeader("Authorization");
        if (auth == null) {
            return null;
        }
        return auth.replace("Bearer ", "");
    }
}