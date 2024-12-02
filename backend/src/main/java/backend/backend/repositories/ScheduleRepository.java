package backend.backend.repositories;

import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByTrainingPlan(TrainingPlan trainingPlan);
}
