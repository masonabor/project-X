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

import org.springframework.web.bind.annotation.*; // Включає PostMapping, PutMapping, DeleteMapping, PathVariable, RequestBody
import java.util.List;  // Клас List для списків
import backend.backend.dtos.UserDTO;  // Ваш DTO для User
import backend.backend.dtos.RegisterUserDto;  // Ваш DTO для реєстрації користувача




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

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersAndCoaches() {
        List<UserDTO> usersAndCoaches = userService.findAllUsersAndCoaches();
        return ResponseEntity.ok(usersAndCoaches);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> addUserOrCoach(@RequestBody RegisterUserDto userDto) {
        User newUser = userService.addUserOrCoach(userDto);
        return ResponseEntity.ok(userService.toDto(newUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUserOrCoach(@PathVariable long id, @RequestBody RegisterUserDto userDto) {
        User updatedUser = userService.updateUserOrCoach(id, userDto);
        return ResponseEntity.ok(userService.toDto(updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserOrCoach(@PathVariable long id) {
        userService.deleteUserOrCoach(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
