package backend.backend.controllers;

import backend.backend.dtos.CoachDTO;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/coaches")
public class CoachController {

    private final UserService userService;

    @GetMapping("/getCoaches")
    public ResponseEntity<?> getCoaches() {
        log.info("Отримання списку тренерів...");
        try {
            return ResponseEntity.ok(userService.findAllByRole("ROLE_COACH"));
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

}