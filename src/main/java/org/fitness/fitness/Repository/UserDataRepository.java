package org.fitness.fitness.Repository;

import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    boolean existsByUser(User user);

    UserData getByUser(User currentUser);
}
