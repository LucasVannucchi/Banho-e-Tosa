package br.com.gihpet.banhoetosa.pets.domain.service;

import br.com.gihpet.banhoetosa.pets.domain.dto.PetRequestDTO;
import br.com.gihpet.banhoetosa.pets.domain.dto.PetResponseDTO;
import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.pets.domain.exceptions.PetAlreadyExistsException;
import br.com.gihpet.banhoetosa.pets.domain.exceptions.PetNotFoundException;
import br.com.gihpet.banhoetosa.pets.domain.mapper.PetMapper;
import br.com.gihpet.banhoetosa.pets.domain.validator.PetValidator;
import br.com.gihpet.banhoetosa.pets.repository.PetRepository;
import br.com.gihpet.banhoetosa.pets.repository.specs.PetSpecs;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserNotFoundException;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final PetValidator petValidator;
    private final UserService userService;

    public PetService(PetRepository petRepository, PetMapper petMapper, PetValidator petValidator, UserService userService) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.petValidator = petValidator;
        this.userService = userService;
    }

    @Transactional
    public PetResponseDTO create(PetRequestDTO dto, User loggedUser, UUID targetUserId) {
        User target = userService.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!loggedUser.getId().equals(targetUserId) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Pet pet = petValidator.validate(petMapper.toEntity(dto));
        pet.setUser(target);
        pet.setCreatedBy(loggedUser.getEmail());
        pet.setCreatedAt(LocalDateTime.now());
        petRepository.save(pet);
        userService.save(target);
        return petMapper.toDTO(pet, loggedUser);
    }

    public Page<PetResponseDTO> list(String namePet, String typeAnimal, String breed,
                                          String color, String bearing, String weight,
                                          String haveAllergy, int page, int size, String sortBy, User loggedUser) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Pet> specs = Specification.where(PetSpecs.namePetLike(namePet))
                .and(PetSpecs.namePetLike(namePet))
                .and(PetSpecs.typeAnimalLike(typeAnimal))
                .and(PetSpecs.breedLike(breed))
                .and(PetSpecs.colorLike(color))
                .and(PetSpecs.bearingLike(bearing))
                .and(PetSpecs.weightLike(weight))
                .and(PetSpecs.haveAllergyLike(haveAllergy));

        if (loggedUser.getRole() == TypeRole.CUSTOMER) {
            specs = specs.and(PetSpecs.userIdEquals(loggedUser.getId()));
        }

        return petRepository.findAll(specs, pageable)
                .map(pet -> petMapper.toDTO(pet, loggedUser));
    }

    @Transactional
    public PetResponseDTO createMyPet(PetRequestDTO dto, User loggedUser, UUID targetUserId) {
        User targetUser = userService.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!loggedUser.getId().equals(targetUserId) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Pet pet = petValidator.validate(petMapper.toEntity(dto));
        if (targetUser.getPets().contains(pet))
            throw new PetAlreadyExistsException("User is already associated with this pet.");

        pet.setUser(targetUser);
        pet.setCreatedBy(loggedUser.getEmail());
        pet.setCreatedAt(LocalDateTime.now());

        petRepository.save(pet);
        userService.save(targetUser);
        return petMapper.toDTO(pet, loggedUser);
    }

    public List<PetResponseDTO> listMyPets(User loggedUser) {
        return loggedUser.getPets().stream()
                .map(p -> petMapper.toDTO(p, loggedUser))
                .toList();
    }

    @Transactional
    public Pet update(Pet pet, UUID idUser, User loggedUser) {
        if (pet.getId() == null)
            throw new IllegalArgumentException("Pet ID required.");

        Pet existing = petRepository.findById(pet.getId())
                .orElseThrow(() -> new PetNotFoundException("Pet not found."));

        User target = userService.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        boolean isOwner = existing.getUser() != null && existing.getUser().getId().equals(loggedUser.getId());

        if (!isOwner && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Optional.ofNullable(pet.getNamePet()).ifPresent(existing::setNamePet);
        Optional.ofNullable(pet.getTypeAnimal()).ifPresent(existing::setTypeAnimal);
        Optional.ofNullable(pet.getBreed()).ifPresent(existing::setBreed);
        Optional.ofNullable(pet.getColor()).ifPresent(existing::setColor);
        Optional.ofNullable(pet.getBearing()).ifPresent(existing::setBearing);
        Optional.ofNullable(pet.getWeight()).ifPresent(existing::setWeight);
        Optional.ofNullable(pet.getHaveAllergy()).ifPresent(existing::setHaveAllergy);
        Optional.ofNullable(pet.getNotes()).ifPresent(existing::setNotes);

        petValidator.validateUpdate(existing);
        existing.setUpdatedBy(loggedUser.getEmail());
        existing.setUpdatedAt(LocalDateTime.now());
        return petRepository.save(existing);
    }

    @Transactional
    public void remove(UUID idUser, UUID idPet, User loggedUser) {
        User user = userService.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Pet pet = petRepository.findById(idPet)
                .orElseThrow(() -> new PetNotFoundException("Pet not found."));

        if (!loggedUser.getId().equals(idUser) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        user.getPets().remove(pet);
        pet.setUser(null);
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        pet.setUpdatedBy(loggedUser.getEmail());
        pet.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
        petRepository.save(pet);
    }

    public Optional<Pet> findById(UUID id) {
        return petRepository.findById(id);
    }
}