package backend.backend.dtos;

import lombok.Data;
import java.util.List;

@Data
public class TrainingPlanDTO {
    private long coachId;
    private String coachName;
    private String coachEmail;
    private boolean acceptedByCoach;
    private List<ScheduleDTO> schedules;
}
