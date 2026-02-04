package br.com.gihpet.banhoetosa.pets.domain.validator;

import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.pets.domain.enums.Bearing;
import br.com.gihpet.banhoetosa.pets.repository.PetRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PetValidator {

    private final PetRepository petRepository;

    public PetValidator(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet validate(Pet pet) {
        if (pet == null) throw new IllegalArgumentException("Pet cannot be null.");
        applyDefaults(pet);

        UUID userId = pet.getUser() != null ? pet.getUser().getId() : null;

        Optional<Pet> existing = petRepository.findByNamePetAndTypeAnimalAndBreedAndColorAndUser_Id(
                pet.getNamePet(), pet.getTypeAnimal(), pet.getBreed(), pet.getColor(), userId
        );

        return existing.orElse(pet);
    }

    public Pet validateUpdate(Pet pet) {
        if (pet == null || pet.getId() == null)
            throw new IllegalArgumentException("Pet ID is mandatory for update.");

        applyDefaults(pet);

        UUID userId = pet.getUser() != null ? pet.getUser().getId() : null;

        Optional<Pet> existing = petRepository.findByNamePetAndTypeAnimalAndBreedAndColorAndUser_Id(
                pet.getNamePet(), pet.getTypeAnimal(), pet.getBreed(), pet.getColor(), userId
        );

        return existing.orElse(pet);
    }

    private void applyDefaults(Pet pet) {
        if (pet.getHaveAllergy() == null) pet.setHaveAllergy(false);
        if (pet.getNotes() == null || pet.getNotes().isBlank()) pet.setNotes("N/A");
        if (pet.getBearing() == null) pet.setBearing(Bearing.MEDIUM);
    }
}