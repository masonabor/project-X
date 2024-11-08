package backend.backend.controllers;

import backend.backend.models.User;
import backend.backend.repositories.UserRepository;
import backend.backend.services.UserService;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String registerUser(@RequestBody User user) throws ValidationException {
        User newUser = userService.registerUser(user);
        userRepository.save(newUser);
        return "Successfully registered";
    }
}
