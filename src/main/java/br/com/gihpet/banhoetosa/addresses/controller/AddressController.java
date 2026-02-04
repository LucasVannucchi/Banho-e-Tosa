package br.com.gihpet.banhoetosa.addresses.controller;

import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressRequestDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseDTO;
import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.addresses.domain.mapper.AddressMapper;
import br.com.gihpet.banhoetosa.addresses.domain.service.AddressService;
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
@RequestMapping("v1/api/addresses")
@Tag(name = "Addresses", description = "Addresses management")
public class AddressController {

    private final AddressService service;
    private final AddressMapper mapper;
    private final UserService userService;

    public AddressController(AddressService service, AddressMapper mapper, UserService userService) {
        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
    }

    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Create address for logged user")
    public ResponseEntity<AddressResponseDTO> createMyAddress
            (@AuthenticationPrincipal UserDetails details,
             @RequestBody @Valid AddressRequestDTO dto) {
        User logged = getAuthenticatedUser(details);
        AddressResponseDTO response = service.createMyAddress(dto, logged, logged.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "List addresses of logged user")
    public ResponseEntity<List<AddressResponseDTO>> listMyAddresses
            (@AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        return ResponseEntity.ok(service.listMyAddresses(logged));
    }

    @PatchMapping("/me/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Update my address")
    public ResponseEntity<AddressResponseDTO> updateMyAddress
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails details,
             @RequestBody @Valid AddressRequestDTO dto) {
        User logged = getAuthenticatedUser(details);
        Address address = mapper.toEntity(dto);
        address.setId(id);
        Address updated = service.update(address, logged.getId(), logged);
        return ResponseEntity.ok(mapper.toDTO(updated, logged));
    }

    @DeleteMapping("/me/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Remove my address")
    public ResponseEntity<Void> removeMyAddress
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        service.remove(logged.getId(), id, logged);
        return ResponseEntity.noContent().build();
    }
}