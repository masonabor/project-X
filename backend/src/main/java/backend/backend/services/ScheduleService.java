package backend.backend.services;

import backend.backend.models.Schedule;
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
}
