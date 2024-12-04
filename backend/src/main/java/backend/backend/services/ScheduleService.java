package backend.backend.services;

import backend.backend.dtos.ScheduleDTO;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void saveAll(List<Schedule> schedules) {
        scheduleRepository.saveAll(schedules);
    }

    public List<Schedule> findAllByTrainingPlan(TrainingPlan trainingPlan) {
        return scheduleRepository.findAllByTrainingPlan(trainingPlan);
    }

    public ScheduleDTO toDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        return scheduleDTO;
    }
}
