package br.com.gihpet.banhoetosa.users.controller;

import br.com.gihpet.banhoetosa.users.domain.dto.UserAttRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserPasswordRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserResponseDTO;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.mapper.UserMapper;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/users")
@Tag(name = "Users", description = "Users management")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    private User getAuthenticatedUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not authenticated"));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO dto) {
        User saved = userService.save(userMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(saved));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Get my user data")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        User logged = getAuthenticatedUser(userDetails);
        return ResponseEntity.ok(userMapper.toDTO(logged));
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Update logged user data")
    public ResponseEntity<UserResponseDTO> updateMe
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody @Valid UserAttRequestDTO dto) {
        User logged = getAuthenticatedUser(userDetails);
        User toUpdate = userMapper.toEntity(dto);
        toUpdate.setId(logged.getId());
        User updated = userService.updateUser(toUpdate, logged);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @PatchMapping("/me/password")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE','ADMIN')")
    @Operation(summary = "Update my password")
    public ResponseEntity<Void> updatePassword
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody @Valid UserPasswordRequestDTO dto) {
        User logged = getAuthenticatedUser(userDetails);
        userService.updatePassword(logged.getId(), dto.password(), logged);
        return ResponseEntity.noContent().build();
    }
}