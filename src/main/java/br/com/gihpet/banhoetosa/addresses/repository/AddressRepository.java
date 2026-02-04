package br.com.gihpet.banhoetosa.addresses.repository;

import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.addresses.domain.enums.TypeHousing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {

    Optional<Address> findByStreetAndNumberAndTypeHousingAndComplementAndNeighborhoodAndCityAndStateAndZipCode(
            String street,
            String number,
            TypeHousing typeHousing,
            String complement,
            String neighborhood,
            String city,
            String state,
            String zipCode
    );
}