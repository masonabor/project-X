package backend.backend.controllers;

import backend.backend.dtos.CoachResponse;
import backend.backend.jwt.JwtUtil;
import backend.backend.models.User;
import backend.backend.services.CoachService;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/coaches")
public class CoachController {

    private final UserService userService;
    private final CoachService coachService;
    private final JwtUtil jwtUtil;

    @GetMapping("/getCoaches")
    public ResponseEntity<?> getCoaches() {
        log.info("Отримання списку тренерів...");
        try {
            List<CoachResponse> coaches = coachService.findAllCoaches();
            if (coaches.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(coaches);
        } catch (Exception e) {
            log.error("Помилка під час отримання тренерів: ", e);
            return ResponseEntity.status(500).body("Помилка на сервері");
        }
    }

    @PatchMapping("/acceptTrainingPlan/{userId}/{accept}")
    public ResponseEntity<?> acceptTrainingPlan(@PathVariable("userId") long userId,
                                                @RequestHeader("Authorization") String header,
                                                @PathVariable("accept") boolean accept) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);
            coachService.acceptTrainingPlan(userId, username, accept);
            if (accept) {
                log.info("Підтвердження тренувального плану для userId: {} користувачем: {}", userId, username);
            } else {
                log.info("Зупинка свівпраці з клієнтом userID: {}", userId);
            }
            return ResponseEntity.ok().build();
        } catch (Error e) {
            log.error("Помилка під час підтвердження тренувального плану: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/getAllClients/{accepted}")
    public ResponseEntity<?> getCoachClients(@RequestHeader("Authorization") String header,
                                             @PathVariable("accepted") boolean accepted) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);
            log.info("Отримання клієнтів тренера: {}", username);
            return ResponseEntity.ok(coachService.getAllClients(username, accepted));
        } catch (Error e) {
            log.error("Помилка при отриманні клієнтів");
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/getClients/{id}")
    public ResponseEntity<?> getCoachClients(@PathVariable("id") long id) {

        try {
            User coach = userService.findById(id).orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
            log.info("Клієнтів тренера: {}", coach.getUsername());
            return ResponseEntity.ok(coachService.getAllClients(coach.getUsername(), true));
        } catch (Error e) {
            log.error("Помилка при отриманні");
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String header) {
        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);
            User coach = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Тренера не знайдено"));
            log.info("Отримання інформації тренера {}" , username);
            return ResponseEntity.ok(coachService.toCoachResponse(coach));
        } catch (Exception e) {
            log.error("Помилка при отриманні інформації про тренера", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/checkCoachInfo/{id}")
    public ResponseEntity<?> getProfile(@PathVariable long id) {
        try {
            User coach = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Тренера не знайдено"));
            log.info("Інформації тренера {}" , coach.getUsername());
            return ResponseEntity.ok(coachService.toCoachResponse(coach));
        } catch (Exception e) {
            log.error("Помилка при отриманні інформації про тренера", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}