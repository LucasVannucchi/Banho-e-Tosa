package br.com.gihpet.banhoetosa.pets.domain.mapper;

import br.com.gihpet.banhoetosa.pets.domain.dto.PetRequestDTO;
import br.com.gihpet.banhoetosa.pets.domain.dto.PetResponseDTO;
import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toEntity(PetRequestDTO dto) {
        if (dto == null) return null;

        Pet pet = new Pet();
        pet.setNamePet(dto.namePet());
        pet.setTypeAnimal(dto.typeAnimal());
        pet.setBreed(dto.breed());
        pet.setColor(dto.color());
        pet.setBearing(dto.bearing());
        pet.setWeight(dto.weight());
        pet.setHaveAllergy(dto.haveAllergy());
        pet.setNotes(dto.notes());
        return pet;
    }

    public PetResponseDTO toDTO(Pet pet, User loggedUser) {
        if (pet == null) return null;

        String ownerName = null;
        User owner = pet.getUser();
        if (loggedUser.getRole() == TypeRole.ADMIN ||
                (owner != null && owner.getId().equals(loggedUser.getId()))) {
            ownerName = owner != null ? owner.getName() : null;
        }

        return new PetResponseDTO(
                pet.getId(),
                pet.getNamePet(),
                pet.getTypeAnimal(),
                pet.getBreed() != null ? pet.getBreed().name() : null,
                pet.getColor(),
                pet.getBearing(),
                pet.getWeight(),
                pet.getHaveAllergy(),
                pet.getNotes(),
                ownerName,
                pet.getCreatedAt(),
                pet.getUpdatedAt()
        );
    }
}