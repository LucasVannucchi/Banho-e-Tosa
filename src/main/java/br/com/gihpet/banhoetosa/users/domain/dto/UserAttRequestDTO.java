package br.com.gihpet.banhoetosa.users.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserAttRequestDTO(
        @Size(min = 2, max = 100)
        String name,
        @Size(min = 2, max = 100)
        String lastName,
        @CPF
        String cpf,
        @Size(min = 9, max = 9)
        String rg,
        @Past
        LocalDate birthDate,
        @Email
        String email,
        @Size(min = 11, max = 15)
        String phone
) { }