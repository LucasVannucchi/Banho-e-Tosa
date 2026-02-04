package br.com.gihpet.banhoetosa.addresses.domain.validator;

import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.addresses.domain.enums.TypeHousing;
import br.com.gihpet.banhoetosa.addresses.repository.AddressRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressValidator {

    private final AddressRepository addressRepository;

    public AddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address validate(Address address) {
        if (address == null) throw new IllegalArgumentException("Address cannot be null.");
        applyDefaults(address);

        Optional<Address> existing = addressRepository.findByStreetAndNumberAndTypeHousingAndComplementAndNeighborhoodAndCityAndStateAndZipCode(
                address.getStreet(),
                address.getNumber(),
                address.getTypeHousing(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );

        return existing.orElse(address);
    }

    public Address validateUpdate(Address address) {
        if (address == null || address.getId() == null)
            throw new IllegalArgumentException("Address ID is mandatory for update.");

        applyDefaults(address);

        Optional<Address> existing = addressRepository.findByStreetAndNumberAndTypeHousingAndComplementAndNeighborhoodAndCityAndStateAndZipCode(
                address.getStreet(),
                address.getNumber(),
                address.getTypeHousing(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );

        return existing.orElse(address);
    }

    private void applyDefaults(Address address) {
        if (address.getTypeHousing() == null) address.setTypeHousing(TypeHousing.HOUSE);
        if (address.getComplement() == null) address.setComplement("N/A");
    }
}