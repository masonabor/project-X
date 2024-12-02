package backend.backend.controllers;

import backend.backend.jwt.JwtUtil;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.services.ScheduleService;
import backend.backend.services.TrainingPlanService;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainingPlans")
public class TrainingPlanController {

    private final ScheduleService scheduleService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/createTrainingPlan")
    public ResponseEntity<?> createSchedule(
            @RequestHeader("Authorization") String header,
            @RequestParam("coachId") long coachId,
            @RequestBody List<Schedule> schedules) {
        try {

            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            TrainingPlan plan = user.getTrainingPlan();

            if (plan == null) {
                return ResponseEntity.badRequest().body("User does not have an assigned training plan.");
            }

            for (Schedule schedule : schedules) {
                schedule.setTrainingPlan(plan);
            }

            scheduleService.saveAll(schedules);

            User coach = userService.findById(coachId)
                    .orElseThrow(() -> new UsernameNotFoundException("Coach not found"));
            plan.setCoach(coach);

            log.info("Schedule successfully created for user: {}", username);
            log.info("Coach with ID {} successfully assigned to user {}.", coachId, username);
            return ResponseEntity.ok("Schedule successfully created.");
        } catch (Exception e) {
            log.error("Error creating training plan: {}", e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while creating the schedule.");
        }
    }
}
