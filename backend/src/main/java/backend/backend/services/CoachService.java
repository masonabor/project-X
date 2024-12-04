package backend.backend.services;

import backend.backend.dtos.ClientsResponse;
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

    public void acceptedTrainingPlan(long userId, String username) {
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

        trainingPlan.setAcceptedByCoach(true);
    }

    public List<ClientsResponse> getAllClients(String username) {
        User coach = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Тренер з username " + username + " не знайдений."));

        List<TrainingPlan> trainingPlans = trainingPlanService.findAllByCoachAndAcceptedByCoach(coach, true);

        return trainingPlans
                .stream()
                .map(userService::toClientsResponse)
                .toList();
    }

}
