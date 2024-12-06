package backend.backend.controllers;

import backend.backend.jwt.JwtUtil;
import backend.backend.models.Role;
import backend.backend.models.User;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/isAdmin")
    public ResponseEntity<?> isAdmin(@RequestHeader("Authorization") String header) {
        if (header.isEmpty()) {
            return ResponseEntity.ok(false);
        }
        String token = jwtUtil.extractTokenFromHeader(header);
        Role role = userService.getRole(jwtUtil.extractUsername(token));
        return role.getRole().equals("ROLE_ADMIN") ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            log.info("Користувач з ідентифікатором {} успішно видалений", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Помилка під час видалення користувача: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PatchMapping("/banUser/{id}")
    public ResponseEntity<?> banUser(@PathVariable long id) {
        try {
            User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
            userService.banUser(id);
            log.info("Користувач {} успішно забанений", user.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Помилка при спробі бану {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PatchMapping("/unbanUser/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable long id) {
        try {
            User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
            userService.unbanUser(id);
            log.info("Користувач {} успішно розбанений", user.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Помилка при спробі розбану {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
