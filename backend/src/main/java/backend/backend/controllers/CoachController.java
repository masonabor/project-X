package backend.backend.controllers;

import backend.backend.dtos.ClientsResponse;
import backend.backend.dtos.CoachDTO;
import backend.backend.jwt.JwtUtil;
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
            List<CoachDTO> coaches = userService.findCoaches();
            if (coaches.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(coaches);
        } catch (Exception e) {
            log.error("Помилка під час отримання тренерів: ", e);
            return ResponseEntity.status(500).body("Помилка на сервері");
        }
    }

    @GetMapping("/getCoachesByDTO")
    public ResponseEntity<List<CoachDTO>> getCoachesByDTO() {
        log.info("Отримання списку тренерів (id, lastName, firstName)...");
        try {
            return ResponseEntity.ok(userService.findCoaches());
        } catch (Exception e) {
            log.error("Помилка під час отримання тренерів: ", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PatchMapping("/acceptTrainingPlan")
    public ResponseEntity<?> acceptTrainingPlan(@RequestParam("userId") long userId,
                                                @RequestHeader("Authorization") String header) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);
            coachService.acceptedTrainingPlan(userId, username);
            log.info("Підтвердження тренувального плану для userId: {} користувачем: {}", userId, username);
            return ResponseEntity.ok().build();
        } catch (Error e) {
            log.error("Помилка під час підтвердження тренувального плану: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/getAllClients")
    public ResponseEntity<List<ClientsResponse>> getCoachClients(@RequestHeader("Authorization") String header) {
        String token = jwtUtil.extractTokenFromHeader(header);
        String username = jwtUtil.extractUsername(token);
        List<ClientsResponse> clients = coachService.getAllClients(username);
        return ResponseEntity.ok(clients);
    }

}