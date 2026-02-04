package br.com.gihpet.banhoetosa.users.domain.validator;

import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.StatusTypeUser;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.exceptions.InvalidUserException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserAlreadyExistsException;
import br.com.gihpet.banhoetosa.users.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(User user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }

        applyDefaults(user);

        if (userExists(user)) {
            throw new UserAlreadyExistsException("A user with the same data already exists.");
        }
    }

    public void validateUpdate(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User ID is mandatory for update.");
        }

        if (userExists(user)) {
            throw new UserAlreadyExistsException("Another user already has this data.");
        }
    }

    private void applyDefaults(User user) {
        if (user.getStatus() == null) user.setStatus(StatusTypeUser.ACTIVE);
        if (user.getRole() == null) user.setRole(TypeRole.CUSTOMER);
    }

    private boolean userExists(User user) {
        Optional<User> existing = userRepository.findByNameAndCpfAndRg(user.getName(), user.getCpf(), user.getRg());
        return existing.isPresent() && (user.getId() == null || !existing.get().getId().equals(user.getId()));
    }
}