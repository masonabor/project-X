package backend.backend.services;

import backend.backend.dtos.ScheduleDTO;
import backend.backend.dtos.TrainingPlanDTO;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.repositories.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
            System.out.println(scheduleDTO.getStartTime());
            System.out.println(scheduleDTO.getEndTime());
            System.out.println(scheduleDTO.isStartTimeBeforeEndTime());
            if (!scheduleDTO.isStartTimeBeforeEndTime()) {
                throw new IllegalArgumentException(
                        "Start time must be before end time for day: " + scheduleDTO.getDayOfWeek()
                );
            }

            Schedule schedule = new Schedule();
            schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());
            schedule.setTrainingPlan(planForStream);
            return schedule;
        }).toList();

        List<DayOfWeek> daysOfWeeks = scheduleService.findAllDayOfWeekByTrainingPlan(planForStream);

        if (!daysOfWeeks.isEmpty()) {
            scheduleService.deleteSchedulesByDayOfWeeks(planForStream, daysOfWeeks);
        }

        scheduleService.saveAll(scheduleEntities);
        plan.setCoach(coach);
        trainingPlanRepository.save(plan);
    }

    public TrainingPlanDTO toDTO(TrainingPlan trainingPlan, List<Schedule> schedules) {
        TrainingPlanDTO trainingPlanDTO = new TrainingPlanDTO();
        trainingPlanDTO.setCoachId(trainingPlan.getCoach() == null ? 0 : trainingPlan.getCoach().getId());
        trainingPlanDTO.setCoachName((trainingPlan.getCoach() == null ? null : trainingPlan.getCoach().getFirstName()));
        trainingPlanDTO.setCoachEmail((trainingPlan.getCoach() == null ? null : trainingPlan.getCoach().getEmail()));
        trainingPlanDTO.setAcceptedByCoach(trainingPlan.isAcceptedByCoach());
        trainingPlanDTO.setSchedules(schedules
                .stream()
                .map(scheduleService::toDTO)
                .toList());
        return trainingPlanDTO;
    }

    public List<TrainingPlan> findAllByCoachAndAcceptedByCoach(User coach, boolean accepted) {
        return trainingPlanRepository.findAllByCoachAndAcceptedByCoach(coach, accepted);
    }

    public void save(TrainingPlan trainingPlan) {
        trainingPlanRepository.save(trainingPlan);
    }
}
