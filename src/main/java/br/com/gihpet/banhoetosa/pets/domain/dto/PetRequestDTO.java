package br.com.gihpet.banhoetosa.pets.domain.dto;

import br.com.gihpet.banhoetosa.pets.domain.enums.AnimalBreed;
import br.com.gihpet.banhoetosa.pets.domain.enums.Bearing;
import br.com.gihpet.banhoetosa.pets.domain.enums.TypeAnimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(
        @NotBlank
        String namePet,
        @NotNull
        TypeAnimal typeAnimal,
        @NotNull
        AnimalBreed breed,
        String color,
        Bearing bearing,
        Double weight,
        Boolean haveAllergy,
        String notes
) { }