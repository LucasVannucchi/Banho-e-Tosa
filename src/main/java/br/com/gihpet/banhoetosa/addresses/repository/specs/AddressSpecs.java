package br.com.gihpet.banhoetosa.addresses.repository.specs;

import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class AddressSpecs {

    /**
     * Querys with Specs
     */

    public static Specification<Address> userIdEquals(UUID userId) {
        return (root, query, cb) -> userId == null
                ? cb.conjunction()
                : cb.equal(root.join("users").get("id"), userId);
    }

    public static Specification<Address> streetLike(String street) {
        return (root, query, cb) ->
                street == null || street.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("street")), "%" + street.toUpperCase() + "%");
    }

    public static Specification<Address> numberLike(String number) {
        return (root, query, cb) ->
                number == null || number.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("number")), "%" + number.toUpperCase() + "%");
    }

    public static Specification<Address> typeHousingLike(String typeHousing) {
        return (root, query, cb) ->
                typeHousing == null || typeHousing.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("typeHousing")), "%" + typeHousing.toUpperCase() + "%");
    }

    public static Specification<Address> complementLike(String complement) {
        return (root, query, cb) ->
                complement == null || complement.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("complement")), "%" + complement.toUpperCase() + "%");
    }

    public static Specification<Address> neighborhoodLike(String neighborhood) {
        return (root, query, cb) ->
                neighborhood == null || neighborhood.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("neighborhood")), "%" + neighborhood.toUpperCase() + "%");
    }

    public static Specification<Address> cityLike(String city) {
        return (root, query, cb) ->
                city == null || city.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("city")), "%" + city.toUpperCase() + "%");
    }

    public static Specification<Address> stateLike(String state) {
        return (root, query, cb) ->
                state == null || state.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("state")), "%" + state.toUpperCase() + "%");
    }

    public static Specification<Address> zipCodeLike(String zipCode) {
        return (root, query, cb) ->
                zipCode == null || zipCode.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("zipCode")), "%" + zipCode.toUpperCase() + "%");
    }
}
