package org.fitness.fitness.Repository;

import java.util.Optional;
import org.fitness.fitness.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);
     Boolean existsByUsername(String username);
     Boolean existsByEmailAndIsVerified(String email, boolean isVerified);

    Boolean existsByEmail(String email);
}
