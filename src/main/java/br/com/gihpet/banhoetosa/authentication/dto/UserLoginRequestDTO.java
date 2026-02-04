package br.com.gihpet.banhoetosa.authentication.dto;

public record UserLoginRequestDTO(
        String email,
        String password
) {
}
