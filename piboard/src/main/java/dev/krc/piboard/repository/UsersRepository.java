package dev.krc.piboard.repository;

import dev.krc.piboard.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findById(int id);
}
