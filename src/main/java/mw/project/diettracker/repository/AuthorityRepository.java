package mw.project.diettracker.repository;

import mw.project.diettracker.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByUsername(String username);

}
