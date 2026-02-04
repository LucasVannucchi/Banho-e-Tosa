package br.com.gihpet.banhoetosa.pets.domain.dto;

import br.com.gihpet.banhoetosa.pets.domain.enums.Bearing;
import br.com.gihpet.banhoetosa.pets.domain.enums.TypeAnimal;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record PetResponseDTO(
        UUID id,
        String namePet,
        TypeAnimal typeAnimal,
        String breed,
        String color,
        Bearing bearing,
        Double weight,
        Boolean haveAllergy,
        String notes,
        String ownerName,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt
) { }