package backend.backend.repositories;

import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(long id);
    List<User> findAllByRole_Role(String role);
    Optional<User> findByTrainingPlan(TrainingPlan trainingPlan);
}
