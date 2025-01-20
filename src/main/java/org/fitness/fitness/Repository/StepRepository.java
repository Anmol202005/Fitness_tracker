package org.fitness.fitness.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.fitness.fitness.Model.Step;
import org.fitness.fitness.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    Optional<Step> findByUserAndDate(User currentUser, LocalDate today);

    List<Step> findByUser(User currentUser);
}
