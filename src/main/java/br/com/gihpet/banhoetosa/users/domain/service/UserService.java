package br.com.gihpet.banhoetosa.users.domain.service;

import br.com.gihpet.banhoetosa.users.domain.dto.UserResponseDTO;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.StatusTypeUser;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserAccessDeniedException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserNotFoundException;
import br.com.gihpet.banhoetosa.users.domain.mapper.UserMapper;
import br.com.gihpet.banhoetosa.users.domain.validator.UserValidator;
import br.com.gihpet.banhoetosa.users.repository.UserRepository;
import br.com.gihpet.banhoetosa.users.repository.specs.UserSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserValidator userValidator, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    @Transactional
    public User save(User user) {
        userValidator.validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedBy(user.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Page<UserResponseDTO> listUsers(
            String name, String lastName,
            String cpf, String phone, String role, String status,
            int page, int size, String sortBy, User loggedUser) {

        if (loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("You don't have permission to list users.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<User> specs = Specification.where(UserSpecs.nameLike(name))
                .and(UserSpecs.lastNameLike(lastName))
                .and(UserSpecs.cpfEquals(cpf))
                .and(UserSpecs.phoneEquals(phone))
                .and(UserSpecs.roleEquals(role))
                .and(UserSpecs.status(status));

        return userRepository.findAll(specs, pageable)
                .map(userMapper::toDTO);
    }

    @Transactional
    public User updateUser(User user, User loggedUser) {
        if (user.getId() == null) throw new IllegalArgumentException("User ID required.");

        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!existing.getId().equals(loggedUser.getId()) && loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("Permission denied.");
        }

        Optional.ofNullable(user.getName()).ifPresent(existing::setName);
        Optional.ofNullable(user.getLastName()).ifPresent(existing::setLastName);
        Optional.ofNullable(user.getCpf()).ifPresent(existing::setCpf);
        Optional.ofNullable(user.getRg()).ifPresent(existing::setRg);
        Optional.ofNullable(user.getBirthDate()).ifPresent(existing::setBirthDate);
        Optional.ofNullable(user.getEmail()).ifPresent(existing::setEmail);
        Optional.ofNullable(user.getPhone()).ifPresent(existing::setPhone);

        userValidator.validateUpdate(existing);
        existing.setUpdatedBy(loggedUser.getEmail());
        existing.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existing);
    }

    @Transactional
    public void updatePassword(UUID id, String rawPassword, User loggedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!user.getId().equals(loggedUser.getId()) && loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("Permission denied.");
        }
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void changeRole(UUID id, TypeRole newRole, User loggedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!user.getId().equals(loggedUser.getId()) && loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("Permission denied.");
        }
        user.setRole(newRole);
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void activate(UUID id, User loggedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!user.getId().equals(loggedUser.getId()) && loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("Permission denied.");
        }
        user.setStatus(StatusTypeUser.ACTIVE);
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void deactivate(UUID id, User loggedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!user.getId().equals(loggedUser.getId()) && loggedUser.getRole() != TypeRole.ADMIN) {
            throw new UserAccessDeniedException("Permission denied.");
        }
        user.setStatus(StatusTypeUser.INACTIVE);
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}