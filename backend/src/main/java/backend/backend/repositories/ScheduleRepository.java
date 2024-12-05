package backend.backend.repositories;

import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByTrainingPlan(TrainingPlan trainingPlan);
    List<Schedule> findAllByTrainingPlan(TrainingPlan trainingPlan);

    @Query("SELECT s.dayOfWeek FROM Schedule s WHERE s.trainingPlan = :trainingPlan")
    List<DayOfWeek> findAllDayOfWeeksByTrainingPlan(TrainingPlan trainingPlan);
    void deleteByTrainingPlanAndDayOfWeekIn(TrainingPlan trainingPlan, List<DayOfWeek> daysOfWeek);
}
