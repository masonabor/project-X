package backend.backend.services;

import backend.backend.models.TrainingPlan;
import backend.backend.models.User;
import backend.backend.repositories.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;

    public void createTrainingPlan(User user) {
        if (user.getRole().getRole().equals("ROLE_USER")) {
            TrainingPlan trainingPlan = new TrainingPlan();
            user.setTrainingPlan(trainingPlan);
            trainingPlanRepository.save(trainingPlan);
        }
    }
}
