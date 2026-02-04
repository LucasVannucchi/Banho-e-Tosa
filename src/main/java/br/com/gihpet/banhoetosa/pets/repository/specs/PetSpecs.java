package br.com.gihpet.banhoetosa.pets.repository.specs;

import br.com.gihpet.banhoetosa.addresses.domain.entity.Address;
import br.com.gihpet.banhoetosa.pets.domain.entity.Pet;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class PetSpecs {

    /**
     * Querys with Specs
     */

    public static Specification<Pet> userIdEquals(UUID userId) {
        return (root, query, cb) ->  userId == null
                ? cb.conjunction()
                : cb.equal(root.join("users").get("id"), userId);
    }

    public static Specification<Pet> namePetLike(String namePet){
        return (root, query, cb) ->
                namePet == null || namePet.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("namePet")), "%" + namePet.toUpperCase() + "%");
    }

    public static Specification<Pet> typeAnimalLike(String typeAnimal){
        return (root, query, cb) ->
                typeAnimal == null || typeAnimal.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("typeAnimal")), "%" + typeAnimal.toUpperCase() + "%");
    }

    public static Specification<Pet> breedLike(String breed){
        return (root, query, cb) ->
                breed == null || breed.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("breed")), "%" + breed.toUpperCase() + "%");
    }

    public static Specification<Pet> colorLike(String color){
        return (root, query, cb) ->
                color == null || color.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("color")), "%" + color.toUpperCase() + "%");
    }

    public static Specification<Pet> bearingLike(String bearing){
        return (root, query, cb) ->
                bearing == null || bearing.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("bearing")), "%" + bearing.toUpperCase() + "%");
    }

    public static Specification<Pet> weightLike(String weight){
        return (root, query, cb) ->
                weight == null || weight.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("weight")), "%" + weight.toUpperCase() + "%");
    }

    public static Specification<Pet> haveAllergyLike(String haveAllergy){
        return (root, query, cb) ->
                haveAllergy == null || haveAllergy.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("haveAllergy")), "%" + haveAllergy.toUpperCase() + "%");
    }
}
