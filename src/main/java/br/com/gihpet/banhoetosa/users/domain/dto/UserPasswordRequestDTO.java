package br.com.gihpet.banhoetosa.users.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserPasswordRequestDTO(
        @NotNull
        @Size(min = 8, max = 20)
        String password
) { }