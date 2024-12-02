package backend.backend.controllers;

import backend.backend.jwt.JwtUtil;
import backend.backend.models.User;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String header) {
        String token = jwtUtil.extractTokenFromHeader(header);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));
        return ResponseEntity.ok(userService.toDto(user));
    }
}
