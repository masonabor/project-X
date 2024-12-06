package backend.backend.controllers;

import backend.backend.dtos.ScheduleDTO;
import backend.backend.dtos.UserGetResponse;
import backend.backend.jwt.JwtUtil;
import backend.backend.mappers.UserMapper;
import backend.backend.models.Role;
import backend.backend.models.User;
import backend.backend.services.ScheduleService;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ScheduleService scheduleService;

    @GetMapping("/profile")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String header) {
        String token = jwtUtil.extractTokenFromHeader(header);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));
        return ResponseEntity.ok(userService.toDto(user));
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String header) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);
            Role role = userService.getRole(username);
            if (role != null) {
                log.info("Користувач {} отримав роль {}", username, role.getRole());
                return ResponseEntity.ok(role.getRole());
            } else {
                log.warn("Роль для користувача {} не знайдена.", username);
                return ResponseEntity.status(404).body("Роль не знайдена.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserGetResponse> users = userService.findAllByRole("ROLE_USER")
                    .stream()
                    .map(UserMapper::toDto)
                    .toList();

            log.info("Успішно отримано {} користувачів з роллю ROLE_USER.", users.size());
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            log.error("Помилка під час обробки запиту: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Виникла помилка на сервері.");
        } catch (Exception e) {
            log.error("Несподівана помилка: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Несподівана помилка.");
        }
    }

    @GetMapping("/checkUserInfo/{id}")
    public ResponseEntity<?> checkUserInfo(@PathVariable long id) {
        try {
            User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            UserGetResponse userGetResponse = UserMapper.toDto(user);
            if (userGetResponse != null) {
                List<ScheduleDTO> schedules = scheduleService.findAllByTrainingPlan(user.getTrainingPlan())
                                .stream()
                                .map(schedule -> {
                                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                                    scheduleDTO.setStartTime(schedule.getStartTime());
                                    scheduleDTO.setEndTime(schedule.getEndTime());
                                    scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
                                    return scheduleDTO;
                                })
                                .toList();

                userGetResponse.setSchedules(schedules);
                log.info("Інформацію {} успішно отримано", userGetResponse.getUsername());
            }

            return ResponseEntity.ok(userGetResponse);
        } catch (Exception e) {
            log.error("Не вдалося отримати інформацію користувача: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
