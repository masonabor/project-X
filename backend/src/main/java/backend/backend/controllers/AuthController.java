package backend.backend.controllers;

import backend.backend.dtos.LoginRequest;
import backend.backend.dtos.LoginResponse;
import backend.backend.jwt.JwtError;
import backend.backend.jwt.JwtUtil;
import backend.backend.models.User;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        registeredUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody User admin) {
        User registeredAdmin = userService.registerAdmin(admin);
        registeredAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return ResponseEntity.ok(registeredAdmin);
    }

    @PostMapping("/registerCoach")
    public ResponseEntity<?> registerCoach(@RequestBody User coach) {
        User registeredCoach = userService.registerCoach(coach);
        registeredCoach.setPassword(passwordEncoder.encode(coach.getPassword()));
        return ResponseEntity.ok(registeredCoach);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new JwtError(HttpStatus.UNAUTHORIZED.value(), "Некоректний логін або пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}

