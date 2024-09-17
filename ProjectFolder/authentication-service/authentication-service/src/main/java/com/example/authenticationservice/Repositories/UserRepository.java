package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
