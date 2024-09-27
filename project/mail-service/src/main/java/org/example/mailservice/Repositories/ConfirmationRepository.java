package org.example.mailservice.Repositories;

import org.example.mailservice.Models.Confirmation;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationRepository extends CrudRepository<Confirmation, Long> {
}
