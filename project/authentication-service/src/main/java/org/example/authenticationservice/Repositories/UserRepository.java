package org.example.authenticationservice.Repositories;

import org.example.authenticationservice.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String username);
}
