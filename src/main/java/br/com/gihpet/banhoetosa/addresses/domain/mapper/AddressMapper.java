package br.com.gihpet.banhoetosa.addresses.domain.mapper;

import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressRequestDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseAdminDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseDTO;
import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDTO dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setTypeHousing(dto.typeHousing());
        address.setComplement(dto.complement());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        return address;
    }

    public AddressResponseDTO toDTO(Address address, User loggedUser) {
        if (address == null) return null;

        List<String> names = address.getUsers().stream()
                .filter(u -> u.getId().equals(loggedUser.getId()))
                .map(User::getName)
                .toList();

        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getTypeHousing(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCreatedAt(),
                address.getUpdatedAt(),
                names
        );
    }

    public AddressResponseAdminDTO toDTOAdmin(Address address, User loggedUser) {
        if (address == null) return null;

        List<String> names = address.getUsers().stream()
                .filter(u -> loggedUser.getRole() == TypeRole.ADMIN || u.getId().equals(loggedUser.getId()))
                .map(User::getName)
                .toList();

        return new AddressResponseAdminDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getTypeHousing(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCreatedAt(),
                address.getUpdatedAt(),
                names
        );
    }
}