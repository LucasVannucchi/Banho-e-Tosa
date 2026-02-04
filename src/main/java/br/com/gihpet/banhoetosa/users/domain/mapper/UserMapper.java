package br.com.gihpet.banhoetosa.users.domain.mapper;

import br.com.gihpet.banhoetosa.users.domain.dto.UserAttRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserRequestDTO;
import br.com.gihpet.banhoetosa.users.domain.dto.UserResponseDTO;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.name());
        user.setLastName(dto.lastName());
        user.setCpf(dto.cpf());
        user.setRg(dto.rg());
        user.setBirthDate(dto.birthDate());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setPhone(dto.phone());
        user.setRole(dto.role());
        user.setStatus(dto.status());
        return user;
    }

    public User toEntity(UserAttRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.name());
        user.setLastName(dto.lastName());
        user.setCpf(dto.cpf());
        user.setRg(dto.rg());
        user.setBirthDate(dto.birthDate());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        return user;
    }

    public UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getCpf(),
                user.getRg(),
                user.getBirthDate(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}