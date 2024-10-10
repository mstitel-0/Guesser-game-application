package org.example.gameeservice.Repositories;

import org.example.gameeservice.Models.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {

}
