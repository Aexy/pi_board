package dev.krc.piboard.repository;

import dev.krc.piboard.model.Issues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssuesRepository extends JpaRepository<Issues,Integer> {

    Optional<Issues> findById(int id);
}
