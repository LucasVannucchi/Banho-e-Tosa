package br.com.gihpet.banhoetosa.users.repository.specs;

import br.com.gihpet.banhoetosa.users.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    /**
     * Querys with Specs
     */

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<User> lastNameLike(String lastName){
        return (root, query, cb) ->
                lastName == null || lastName.isEmpty()
                        ? cb.conjunction()
                        : cb.like(cb.upper(root.get("lastName")), "%" + lastName.toUpperCase() + "%");
    }

    public static Specification<User> cpfEquals(String cpf){
        return (root, query, cb) ->
                cpf == null || cpf.isEmpty()
                        ? cb.conjunction()
                        : cb.equal(root.get("cpf"), cpf);
    }

    public static Specification<User> phoneEquals(String phone){
        return (root, query, cb) ->
                phone == null || phone.isEmpty()
                        ? cb.conjunction()
                        : cb.equal(root.get("phone"), phone);
    }

    public static Specification<User> roleEquals(String role){
        return (root, query, cb) ->
                role == null || role.isEmpty()
                        ? cb.conjunction()
                        : cb.equal(root.get("role"), role);
    }

    public static Specification<User> status(String status){
        return (root, query, cb) ->
                status == null || status.isEmpty()
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }
}
