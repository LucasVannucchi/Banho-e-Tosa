package br.com.gihpet.banhoetosa.addresses.domain.dto;

import br.com.gihpet.banhoetosa.addresses.domain.enums.TypeHousing;
import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        @NotBlank String street,
        String number,
        TypeHousing typeHousing,
        String complement,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String zipCode
) { }