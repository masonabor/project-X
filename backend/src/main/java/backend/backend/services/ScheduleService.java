package backend.backend.services;

import backend.backend.dtos.ScheduleDTO;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.repositories.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void saveAll(List<Schedule> schedules) {
        scheduleRepository.saveAll(schedules);
    }

    @Transactional
    public List<Schedule> findAllByTrainingPlan(TrainingPlan trainingPlan) {
        return scheduleRepository.findAllByTrainingPlan(trainingPlan);
    }

    @Transactional
    public List<DayOfWeek> findAllDayOfWeekByTrainingPlan(TrainingPlan trainingPlan) {
        return scheduleRepository.findAllDayOfWeeksByTrainingPlan(trainingPlan);
    }

    @Transactional
    public void deleteSchedulesByDayOfWeeks(TrainingPlan trainingPlan, List<DayOfWeek> daysOfWeeks) {
        scheduleRepository.deleteByTrainingPlanAndDayOfWeekIn(trainingPlan, daysOfWeeks);
    }

    public ScheduleDTO toDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        return scheduleDTO;
    }
}
