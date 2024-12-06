package backend.backend.services;

import backend.backend.dtos.ClientsResponse;
import backend.backend.dtos.CoachResponse;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import backend.backend.dtos.CoachDTO;
import backend.backend.repositories.RoleRepository;




@Service
@RequiredArgsConstructor
public class CoachService {

    private final RoleRepository roleRepository;

    private final UserService userService;
    private final TrainingPlanService trainingPlanService;

    public void acceptTrainingPlan(long userId, String username, boolean accept) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Користувач з ID " + userId + " не знайдений."));

        User coach = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Тренер з username " + username + " не знайдений."));

        TrainingPlan trainingPlan = user.getTrainingPlan();
        if (trainingPlan == null) {
            throw new IllegalStateException("Користувач з ID " + userId + " не має тренувального плану.");
        }

        if (!trainingPlan.getCoach().equals(coach)) {
            throw new IllegalArgumentException("Тренер " + username + " не відповідає тренеру, закріпленому за цим планом.");
        }

        trainingPlan.setAcceptedByCoach(accept);
        trainingPlanService.save(trainingPlan);
    }

    public List<ClientsResponse> getAllClients(String username, boolean accepted) {
        User coach = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Тренер з username " + username + " не знайдений."));

        List<TrainingPlan> trainingPlans = trainingPlanService.findAllByCoachAndAcceptedByCoach(coach, accepted);

        return trainingPlans
                .stream()
                .map(userService::toClientsResponse)
                .toList();
    }

    public CoachResponse toCoachResponse(User coach) {
        CoachResponse coachResponse = new CoachResponse();
        coachResponse.setUsername(coach.getUsername());
        coachResponse.setEmail(coach.getEmail());
        coachResponse.setPhone(coach.getPhone());
        coachResponse.setFirstName(coach.getFirstName());
        coachResponse.setLastName(coach.getLastName());
        coachResponse.setMiddleName(coach.getMiddleName());
        coachResponse.setDateOfBirth(coach.getDateOfBirth());
        coachResponse.setGender(coachResponse.getGender());
        coachResponse.setRole(coach.getRole().getRole());
        return coachResponse;
    }



    public void addCoach(CoachDTO coachDTO) {
        User coach = new User();
        coach.setUsername(coachDTO.getFirstName().toLowerCase() + "." + coachDTO.getLastName().toLowerCase());
        coach.setFirstName(coachDTO.getFirstName());
        coach.setLastName(coachDTO.getLastName());
        coach.setRole(roleRepository.findByRole("ROLE_COACH")
                .orElseThrow(() -> new IllegalArgumentException("Роль 'ROLE_COACH' не знайдена")));
        userService.save(coach);

    }

    public void updateCoach(Long coachId, CoachDTO coachDTO) {
        User coach = userService.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException("Тренера з ID " + coachId + " не знайдено."));

        coach.setFirstName(coachDTO.getFirstName());
        coach.setLastName(coachDTO.getLastName());
        userService.save(coach);
    }

}
