package br.com.gihpet.banhoetosa.pets.controller;

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
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/api/pets")
@Tag(name = "Pets", description = "Pets management")
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;
    private final UserService userService;

    public PetController(PetService petService, PetMapper petMapper, UserService userService) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.userService = userService;
    }


    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Create pet for logged user")
    public ResponseEntity<PetResponseDTO> createMyPet
            (@AuthenticationPrincipal UserDetails details,
             @RequestBody @Valid PetRequestDTO dto) {
        User logged = getAuthenticatedUser(details);
        PetResponseDTO response = petService.createMyPet(dto, logged, logged.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "List pets of logged user")
    public ResponseEntity<List<PetResponseDTO>> listMyPets
            (@AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        return ResponseEntity.ok(petService.listMyPets(logged));
    }

    @PatchMapping("/me/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Update my pet")
    public ResponseEntity<PetResponseDTO> updateMyPet
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails details,
             @RequestBody @Valid PetRequestDTO dto) {
        User logged = getAuthenticatedUser(details);
        Pet pet = petMapper.toEntity(dto);
        pet.setId(id);
        Pet updated = petService.update(pet, logged.getId(), logged);
        return ResponseEntity.ok(petMapper.toDTO(updated, logged));
    }

    @DeleteMapping("/me/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Remove my pet")
    public ResponseEntity<Void> removeMyPet
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        petService.remove(logged.getId(), id, logged);
        return ResponseEntity.noContent().build();
    }
}