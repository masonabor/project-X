package backend.backend.controllers;

import backend.backend.models.User;
import backend.backend.repositories.UserRepository;
import backend.backend.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user, HttpSession session) throws ValidationException {
        User newUser = userService.registerUser(user);
        userRepository.save(newUser);
        session.setAttribute(newUser.getEmail(), newUser);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<User> login(@PathVariable String email, HttpSession session) {
        User user = (User) session.getAttribute(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
