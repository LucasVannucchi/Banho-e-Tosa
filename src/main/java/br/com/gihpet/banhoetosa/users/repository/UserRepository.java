package br.com.gihpet.banhoetosa.users.repository;

import br.com.gihpet.banhoetosa.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNameAndCpfAndRg(String name, String cpf, String rg);

}