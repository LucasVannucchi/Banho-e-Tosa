package br.com.gihpet.banhoetosa.addresses.controller;

import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressRequestDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseAdminDTO;
import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.addresses.domain.mapper.AddressMapper;
import br.com.gihpet.banhoetosa.addresses.domain.service.AddressService;
import br.com.gihpet.banhoetosa.common.domain.dto.pagination.PagedResponse;
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
@RequestMapping("v1/api/addresses/admin")
@Tag(name = "Addresses Admin", description = "Administrative management for addresses")
public class AddressAdminController {

    private final AddressService service;
    private final AddressMapper mapper;
    private final UserService userService;

    public AddressAdminController(AddressService service, AddressMapper mapper, UserService userService) {
        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
    }

    private User getAuthenticatedUser(UserDetails details) {
        return userService.findByEmail(details.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create address for a user (ADMIN only)")
    public ResponseEntity<AddressResponseAdminDTO> createAddressForUser
            (@PathVariable("id") UUID id,
             @RequestBody @Valid AddressRequestDTO dto,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        var response = service.create(dto, logged, id);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all addresses")
    public ResponseEntity<PagedResponse<AddressResponseAdminDTO>> listAll
            (
                    @RequestParam(value = "street", required = false) String street,
                    @RequestParam(value = "number", required = false) String number,
                    @RequestParam(value = "typeHousing", required = false) String typeHousing,
                    @RequestParam(value = "complement", required = false) String complement,
                    @RequestParam(value = "neighborhood", required = false) String neighborhood,
                    @RequestParam(value = "city", required = false) String city,
                    @RequestParam(value = "state", required = false) String state,
                    @RequestParam(value = "zipCode", required = false) String zipCode,
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size,
                    @RequestParam(value = "sortBy", defaultValue = "street") String sortBy,
                    @AuthenticationPrincipal UserDetails userDetails) {

        User loggedUser = getAuthenticatedUser(userDetails);

        Page<AddressResponseAdminDTO> addresses = service.list(
                street, number, typeHousing, complement,
                neighborhood, city, state, zipCode,
                page, size, sortBy, loggedUser);

        PagedResponse<AddressResponseAdminDTO> answer = new PagedResponse<>(
                addresses.getPageable().getPageNumber(),
                addresses.getPageable().getPageSize(),
                addresses.getTotalElements(),
                addresses.getTotalPages(),
                addresses.getContent());

        return ResponseEntity.ok(answer);
    }


    @PatchMapping("/users/{idUser}/addresses/{idAddress}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update address of a user")
    public ResponseEntity<AddressResponseAdminDTO> updateAddressOfUser
            (@PathVariable("idUser") UUID idUser,
             @PathVariable("idAddress") UUID idAddress,
             @RequestBody @Valid AddressRequestDTO dto,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        Address address = mapper.toEntity(dto);
        address.setId(idAddress);
        Address updated = service.update(address, idUser, logged);
        return ResponseEntity.ok(mapper.toDTOAdmin(updated, logged));
    }

    @DeleteMapping("/users/{idUser}/addresses/{idAddress}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove address of user")
    public ResponseEntity<Void> removeAddressOfUser
            (@PathVariable("idUser") UUID idUser,
             @PathVariable("idAddress") UUID idAddress,
             @AuthenticationPrincipal UserDetails details) {
        User logged = getAuthenticatedUser(details);
        service.remove(idUser, idAddress, logged);
        return ResponseEntity.noContent().build();
    }
}