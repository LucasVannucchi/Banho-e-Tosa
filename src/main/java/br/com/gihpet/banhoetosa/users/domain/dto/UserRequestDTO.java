package br.com.gihpet.banhoetosa.users.domain.dto;

import br.com.gihpet.banhoetosa.users.domain.enums.StatusTypeUser;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequestDTO(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,

        @NotBlank
        @Size(min = 2, max = 100)
        String lastName,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        @Size(min = 7, max = 11)
        String rg,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String password,

        TypeRole role,

        @NotBlank
        @Size(min = 11, max = 11)
        String phone,

        StatusTypeUser status
) { }