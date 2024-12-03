package backend.backend.controllers;

import backend.backend.dtos.CreateTrainingPlanRequest;
import backend.backend.jwt.JwtUtil;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.services.ScheduleService;
import backend.backend.services.TrainingPlanService;
import backend.backend.services.UserService;
import jakarta.validation.Valid;
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
    private final TrainingPlanService trainingPlanService;

    @PostMapping("/createTrainingPlan")
    public ResponseEntity<?> createSchedule(
            @RequestHeader("Authorization") String header,
            @RequestBody @Valid CreateTrainingPlanRequest request) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            User coach = userService.findById(request.getCoachId())
                    .orElseThrow(() -> new IllegalArgumentException("Coach not found"));

            trainingPlanService.createTrainingPlan(user, coach, request.getSchedules());

            log.info("Schedule successfully created for user: {}", username);
            return ResponseEntity.ok("Schedule successfully created.");
        } catch (UsernameNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


    @GetMapping("/getTrainingPlan")
    public ResponseEntity<?> getTrainingPlan(@RequestHeader("Authorization") String header) {
        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            TrainingPlan plan = user.getTrainingPlan();
            if (plan == null) {
                log.info("Training plan not found for user: {}", username);
                return ResponseEntity.status(404).body("Training plan not found.");
            }

            List<Schedule> schedules = scheduleService.findAllByTrainingPlan(plan);

            return ResponseEntity.ok(trainingPlanService.toDTO(plan, schedules));
        } catch (Exception e) {
            log.error("Error getting training plan: {}", e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while getting the training plan.");
        }
    }

}
