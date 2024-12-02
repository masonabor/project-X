package backend.backend.controllers;

import backend.backend.jwt.JwtUtil;
import backend.backend.models.Role;
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
        String token = jwtUtil.extractTokenFromHeader(header);
        Role role = userService.getRole(jwtUtil.extractUsername(token));
        return role.getRole().equals("ROLE_ADMIN") ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }
}
