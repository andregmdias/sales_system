package br.com.giannatech.salessystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.giannatech.salessystem.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByCpfCnpj(String cpfCnpj);

}
