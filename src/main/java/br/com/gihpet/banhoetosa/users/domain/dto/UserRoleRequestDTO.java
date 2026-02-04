package br.com.gihpet.banhoetosa.users.domain.dto;

import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleRequestDTO(
        @NotNull
        TypeRole role
) { }