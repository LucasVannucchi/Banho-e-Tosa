package br.com.gihpet.banhoetosa.pets.controller;

import br.com.gihpet.banhoetosa.common.domain.dto.pagination.PagedResponse;
import br.com.gihpet.banhoetosa.pets.domain.dto.PetRequestDTO;
import br.com.gihpet.banhoetosa.pets.domain.dto.PetResponseDTO;
import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import br.com.gihpet.banhoetosa.pets.domain.mapper.PetMapper;
import br.com.gihpet.banhoetosa.pets.domain.service.PetService;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/api/pets/admin")
@Tag(name = "Pets Admin", description = "Administrative management for pets")
public class PetAdminController {

    private final PetService petService;
    private final PetMapper petMapper;
    private final UserService userService;

    public PetAdminController(PetService petService, PetMapper petMapper, UserService userService) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.userService = userService;
    }

    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create pet for another user (ADMIN only)")
    public ResponseEntity<PetResponseDTO> createPetForUser
            (@PathVariable("idUser") UUID idUser,
             @RequestBody @Valid PetRequestDTO dto,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        return ResponseEntity.status(201).body(petService.create(dto, logged, idUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all pets")
    public ResponseEntity<PagedResponse<PetResponseDTO>> listPets
            (@RequestParam(value = "namePet", required = false) String namePet,
             @RequestParam(value = "typeAnimal", required = false) String typeAnimal,
             @RequestParam(value = "breed", required = false) String breed,
             @RequestParam(value = "color", required = false) String color,
             @RequestParam(value = "bearing", required = false) String bearing,
             @RequestParam(value = "weight", required = false) String weight,
             @RequestParam(value = "haveAllergy", required = false) String haveAllergy,
             @RequestParam(value = "page", defaultValue = "0") int page,
             @RequestParam(value = "size", defaultValue = "10") int size,
             @RequestParam(value = "sortBy", defaultValue = "namePet") String sortBy,
             @AuthenticationPrincipal UserDetails userDetails) {

        User loggedUser = getAuthenticatedUser(userDetails);

        Page<PetResponseDTO> pets = petService.list(
                namePet, typeAnimal, breed, color, bearing,
                weight, haveAllergy, page, size, sortBy, loggedUser);

        PagedResponse<PetResponseDTO> answer = new PagedResponse<>(
                pets.getPageable().getPageNumber(),
                pets.getPageable().getPageSize(),
                pets.getTotalElements(),
                pets.getTotalPages(),
                pets.getContent());

        return ResponseEntity.ok(answer);
    }

    @PatchMapping("/users/{idUser}/pets/{idPet}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update pet of a user")
    public ResponseEntity<PetResponseDTO> updatePetOfUser
            (@PathVariable("idUser") UUID idUser,
             @PathVariable("idPet") UUID idPet,
             @RequestBody @Valid PetRequestDTO dto,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        Pet pet = petMapper.toEntity(dto);
        pet.setId(idPet);
        Pet updated = petService.update(pet, idUser, logged);
        return ResponseEntity.ok(petMapper.toDTO(updated, logged));
    }

    @DeleteMapping("/users/{idUser}/pets/{idPet}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove pet from user")
    public ResponseEntity<Void> removePetOfUser
            (@PathVariable("idUser") UUID idUser,
             @PathVariable("idPet") UUID idPet,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        petService.remove(idUser, idPet, logged);
        return ResponseEntity.noContent().build();
    }
}