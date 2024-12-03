package backend.backend.services;

import backend.backend.dtos.ScheduleDTO;
import backend.backend.dtos.TrainingPlanDTO;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.repositories.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final ScheduleService scheduleService;

    public void createTrainingPlan(User user) {
        if (user.getRole().getRole().equals("ROLE_USER")) {
            TrainingPlan trainingPlan = new TrainingPlan();
            trainingPlan.setAcceptedByCoach(false);
            user.setTrainingPlan(trainingPlan);
            trainingPlanRepository.save(trainingPlan);
        }
    }

    public void createTrainingPlan(User user, User coach, List<ScheduleDTO> schedules) {
        TrainingPlan plan = user.getTrainingPlan();

        if (plan == null) {
            plan = new TrainingPlan();
            plan.setAcceptedByCoach(false);
            trainingPlanRepository.save(plan);
        }

        final TrainingPlan planForStream = plan;

        List<Schedule> scheduleEntities = schedules.stream().map(scheduleDTO -> {
            Schedule schedule = new Schedule();
            schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());
            schedule.setTrainingPlan(planForStream);
            return schedule;
        }).toList();

        scheduleService.saveAll(scheduleEntities);
        plan.setCoach(coach);
        trainingPlanRepository.save(plan);
    }

    public TrainingPlanDTO toDTO(TrainingPlan trainingPlan, List<Schedule> schedules) {
        TrainingPlanDTO trainingPlanDTO = new TrainingPlanDTO();
        trainingPlanDTO.setCoachName((trainingPlan.getCoach() == null ? null : trainingPlan.getCoach().getFirstName()));
        trainingPlanDTO.setCoachEmail((trainingPlan.getCoach() == null ? null : trainingPlan.getCoach().getEmail()));
        trainingPlanDTO.setSchedules(schedules
                .stream()
                .map(scheduleService::toDTO)
                .toList());
        return trainingPlanDTO;
    }


}