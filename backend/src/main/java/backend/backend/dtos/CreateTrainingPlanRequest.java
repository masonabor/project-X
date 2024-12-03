package backend.backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateTrainingPlanRequest {
    @NotNull(message = "Coach ID is required")
    private Long coachId;

    @NotEmpty(message = "Schedule list cannot be empty")
    private List<ScheduleDTO> schedules;
}
