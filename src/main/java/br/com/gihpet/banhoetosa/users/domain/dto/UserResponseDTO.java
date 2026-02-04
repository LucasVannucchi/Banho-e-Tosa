package br.com.gihpet.banhoetosa.users.domain.dto;

import br.com.gihpet.banhoetosa.users.domain.enums.StatusTypeUser;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String lastName,
        String cpf,
        String rg,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,
        String email,
        TypeRole role,
        String phone,
        StatusTypeUser status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt
) { }