package backend.backend.services;

import backend.backend.HashPassword;
import backend.backend.models.User;
import backend.backend.repositories.UserRepository;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) throws ValidationException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("Email is already in use");
        }
        user.setPassword(new HashPassword().hash(user.getPassword()));
        return userRepository.save(user);
    }
}
