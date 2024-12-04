package backend.backend.services;

import backend.backend.dtos.*;
import backend.backend.models.Role;
import backend.backend.models.Schedule;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.repositories.RoleRepository;
import backend.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountService accountService;
    private final TrainingPlanService trainingPlanService;
    private final ScheduleService scheduleService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllByRole(String role) {
        return userRepository.findAllByRole_Role(role);
    }

    public User findByTrainingPlan(TrainingPlan trainingPlan) {
        return userRepository.findByTrainingPlan(trainingPlan).orElse(null);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User %s not found", username)
        ));
        String role = user.getRole().getRole();
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities) {
        };
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists!");
        }
        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));
        user.setRole(userRole);
        user.setAccount(accountService.createAccount());
        trainingPlanService.createTrainingPlan(user);
        return userRepository.save(user);
    }

    public User registerAdmin(User admin) {
        if (userRepository.findByUsername(admin.getUsername()).isPresent() || userRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists!");
        }
        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Default role ADMIN not found"));
        admin.setRole(adminRole);
        return userRepository.save(admin);
    }

    public User registerCoach(User coach) {
        if (userRepository.findByUsername(coach.getUsername()).isPresent() || userRepository.findByEmail(coach.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists!");
        }
        Role coachRole = roleRepository.findByRole("ROLE_COACH")
                .orElseThrow(() -> new RuntimeException("Default role COACH not found"));
        coach.setRole(coachRole);
        return userRepository.save(coach);
    }

    public Role getRole(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));
        return user.getRole();
    }

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMiddleName(user.getMiddleName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());

        UserDTO.RoleDTO roleDTO = new UserDTO.RoleDTO();
        roleDTO.setRole(user.getRole().getRole());
        dto.setRole(roleDTO);

        UserDTO.AccountDTO accountDTO = new UserDTO.AccountDTO();
        accountDTO.setAccountNumber(user.getAccount().getAccountNumber());
        dto.setAccount(accountDTO);

        return dto;
    }

    public List<CoachDTO> findCoaches() {
        return findAllByRole("ROLE_COACH").stream()
                .map(user -> new CoachDTO(user.getId(), user.getLastName(), user.getFirstName()))
                .collect(Collectors.toList());
    }

    public ClientsResponse toClientsResponse(TrainingPlan trainingPlan) {

        List<Schedule> schedules = scheduleService.findAllByTrainingPlan(trainingPlan);

        List<ScheduleDTO> schedulesDto = schedules
                .stream()
                .map(schedule -> {
                        ScheduleDTO scheduleDTO = new ScheduleDTO();
                        scheduleDTO.setStartTime(schedule.getStartTime());
                        scheduleDTO.setEndTime(schedule.getEndTime());
                        scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
                        return scheduleDTO;
                })
                .toList();

        ClientsResponse.TrainingPlanDTO trainingPlanDTO = new ClientsResponse.TrainingPlanDTO();
        trainingPlanDTO.setSchedules(schedulesDto);

        ClientsResponse clientsResponse = new ClientsResponse();
        User client = findByTrainingPlan(trainingPlan);

        if (client == null) {
            throw new RuntimeException("Training plan not found");
        }
        clientsResponse.setUsername(client.getUsername());
        clientsResponse.setEmail(client.getEmail());
        clientsResponse.setPhone(client.getPhone());
        clientsResponse.setFirstName(client.getFirstName());
        clientsResponse.setLastName(client.getLastName());
        clientsResponse.setMiddleName(client.getMiddleName());
        clientsResponse.setDateOfBirth(client.getDateOfBirth());
        clientsResponse.setGender(client.getGender());
        clientsResponse.setTrainingPlan(trainingPlanDTO);
        return clientsResponse;
    }
}

