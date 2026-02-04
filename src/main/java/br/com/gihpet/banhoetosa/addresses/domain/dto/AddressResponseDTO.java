package br.com.gihpet.banhoetosa.addresses.domain.dto;

import br.com.gihpet.banhoetosa.addresses.domain.enums.TypeHousing;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String street,
        String number,
        TypeHousing typeHousing,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt,
        List<String> userNames
) { }