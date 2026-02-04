package br.com.gihpet.banhoetosa.addresses.domain.service;

import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressRequestDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseAdminDTO;
import br.com.gihpet.banhoetosa.addresses.domain.dto.AddressResponseDTO;
import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.addresses.domain.mapper.AddressMapper;
import br.com.gihpet.banhoetosa.addresses.domain.validator.AddressValidator;
import br.com.gihpet.banhoetosa.addresses.repository.AddressRepository;
import br.com.gihpet.banhoetosa.addresses.repository.specs.AddressSpecs;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import br.com.gihpet.banhoetosa.users.domain.enums.TypeRole;
import br.com.gihpet.banhoetosa.users.domain.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AddressValidator addressValidator;
    private final UserService userService;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper, AddressValidator addressValidator, UserService userService) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.addressValidator = addressValidator;
        this.userService = userService;
    }

    @Transactional
    public AddressResponseAdminDTO create(AddressRequestDTO dto, User loggedUser, UUID targetUserId) {
        User target = userService.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        if (!loggedUser.getId().equals(targetUserId) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Address address = addressValidator.validate(addressMapper.toEntity(dto));
        if (target.getAddresses().contains(address))
            throw new IllegalArgumentException("User is already associated with this address.");

        address.setCreatedBy(loggedUser.getEmail());
        address.setCreatedAt(LocalDateTime.now());
        target.getAddresses().add(address);
        address.getUsers().add(target);

        addressRepository.save(address);
        userService.save(target);
        return addressMapper.toDTOAdmin(address, loggedUser);
    }

    public Page<AddressResponseAdminDTO> list(
            String street, String number, String typeHouse, String complement,
            String neighborhood, String city, String state, String zipCode,
            int page, int size, String sortBy, User loggedUser) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Specification<Address> specs = Specification.where(AddressSpecs.streetLike(street))
                .and(AddressSpecs.streetLike(street))
                .and(AddressSpecs.numberLike(number))
                .and(AddressSpecs.typeHousingLike(typeHouse))
                .and(AddressSpecs.complementLike(complement))
                .and(AddressSpecs.neighborhoodLike(neighborhood))
                .and(AddressSpecs.cityLike(city))
                .and(AddressSpecs.stateLike(state))
                .and(AddressSpecs.zipCodeLike(zipCode));

        if (loggedUser.getRole() == TypeRole.CUSTOMER) {
            specs = specs.and(AddressSpecs.userIdEquals(loggedUser.getId()));
        }

        return addressRepository.findAll(specs, pageable)
                .map(address -> addressMapper.toDTOAdmin(address, loggedUser));
    }

    @Transactional
    public AddressResponseDTO createMyAddress(AddressRequestDTO dto, User loggedUser, UUID targetUserId) {
        User targetUser = userService.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        if (!loggedUser.getId().equals(targetUserId) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Address address = addressValidator.validate(addressMapper.toEntity(dto));
        if (targetUser.getAddresses().contains(address))
            throw new IllegalArgumentException("User is already associated with this address.");

        address.setCreatedBy(loggedUser.getEmail());
        address.setCreatedAt(LocalDateTime.now());
        targetUser.getAddresses().add(address);
        address.getUsers().add(targetUser);

        addressRepository.save(address);
        userService.save(targetUser);
        return addressMapper.toDTO(address, loggedUser);
    }

    public List<AddressResponseDTO> listMyAddresses(User loggedUser) {
        return loggedUser.getAddresses().stream()
                .map(a -> addressMapper.toDTO(a, loggedUser))
                .toList();
    }

    @Transactional
    public Address update(Address address, UUID idUser, User loggedUser) {
        if (address.getId() == null)
            throw new IllegalArgumentException("Address ID is required.");

        Address existing = addressRepository.findById(address.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found."));
        User user = userService.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        boolean isOwner = existing.getUsers().stream().anyMatch(u -> u.getId().equals(loggedUser.getId()));
        if (!isOwner && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        Optional.ofNullable(address.getStreet()).ifPresent(existing::setStreet);
        Optional.ofNullable(address.getNumber()).ifPresent(existing::setNumber);
        Optional.ofNullable(address.getTypeHousing()).ifPresent(existing::setTypeHousing);
        Optional.ofNullable(address.getComplement()).ifPresent(existing::setComplement);
        Optional.ofNullable(address.getNeighborhood()).ifPresent(existing::setNeighborhood);
        Optional.ofNullable(address.getCity()).ifPresent(existing::setCity);
        Optional.ofNullable(address.getState()).ifPresent(existing::setState);
        Optional.ofNullable(address.getZipCode()).ifPresent(existing::setZipCode);

        addressValidator.validateUpdate(existing);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(loggedUser.getEmail());
        return addressRepository.save(existing);
    }

    @Transactional
    public void remove(UUID idUser, UUID idAddress, User loggedUser) {
        User user = userService.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Address address = addressRepository.findById(idAddress)
                .orElseThrow(() -> new IllegalArgumentException("Address not found."));
        if (!loggedUser.getId().equals(idUser) && loggedUser.getRole() != TypeRole.ADMIN)
            throw new AccessDeniedException("Permission denied.");

        user.getAddresses().remove(address);
        address.getUsers().remove(user);
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        address.setUpdatedBy(loggedUser.getEmail());
        address.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
        addressRepository.save(address);
    }
}