package backend.backend.services;

import backend.backend.dtos.ClientsResponse;
import backend.backend.dtos.CoachResponse;
import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

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
        coachResponse.setId(coach.getId());
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

    public List<CoachResponse> findAllCoaches() {
        return userService.findAllByRole("ROLE_COACH").stream()
                .map(this::toCoachResponse).toList();
    }
}
