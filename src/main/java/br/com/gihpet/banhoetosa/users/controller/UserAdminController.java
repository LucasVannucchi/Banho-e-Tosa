package br.com.gihpet.banhoetosa.users.controller;

import br.com.gihpet.banhoetosa.common.domain.dto.pagination.PagedResponse;
import br.com.gihpet.banhoetosa.users.domain.dto.UserResponseDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserRoleRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.mapper.UserMapper;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/api/users/admin")
@Tag(name = "Users Admin", description = "Admin management for users")
public class UserAdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserAdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Find a user by id")
    public ResponseEntity<UserResponseDTO> findById
            (@PathVariable("id") UUID id) {
        return userService.findById(id)
                .map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users")
    public ResponseEntity<PagedResponse<UserResponseDTO>> findAllUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @AuthenticationPrincipal UserDetails userDetails) {

        User loggedUser = getAuthenticatedUser(userDetails);

        Page<UserResponseDTO> usersPage = userService.listUsers(
                name, lastName, cpf, phone, role, status, page,
                size, sortBy, loggedUser);

        PagedResponse<UserResponseDTO> answer = new PagedResponse<>(
                usersPage.getPageable().getPageNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.getContent()
        );

        return ResponseEntity.ok(answer);
    }


    @PatchMapping("{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user role")
    public ResponseEntity<Void> changeRole
            (@PathVariable("id") UUID id,
             @RequestBody @Valid UserRoleRequestDTO dto,
             @AuthenticationPrincipal UserDetails userDetails) {
        User logged = getAuthenticatedUser(userDetails);
        userService.changeRole(id, dto.role(), logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivate user")
    public ResponseEntity<Void> activate
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails userDetails) {
        User logged = getAuthenticatedUser(userDetails);
        userService.activate(id, logged);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user")
    public ResponseEntity<Void> deactivate
            (@PathVariable("id") UUID id,
             @AuthenticationPrincipal UserDetails userDetails) {
        User logged = getAuthenticatedUser(userDetails);
        userService.deactivate(id, logged);
        return ResponseEntity.noContent().build();
    }
}