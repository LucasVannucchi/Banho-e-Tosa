package br.com.gihpet.banhoetosa.pets.repository;

import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.pets.domain.enums.AnimalBreed;
import br.com.gihpet.banhoetosa.pets.domain.enums.TypeAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID>, JpaSpecificationExecutor<Pet> {

    Optional<Pet> findByNamePetAndTypeAnimalAndBreedAndColorAndUser_Id(
            String namePet,
            TypeAnimal typeAnimal,
            AnimalBreed breed,
            String color,
            UUID userId
    );
}