package backend.backend.repositories;

import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    Optional<TrainingPlan> findById(long id);
    List<TrainingPlan> findAllByCoachAndAcceptedByCoach(User coach, boolean accepted);
}
