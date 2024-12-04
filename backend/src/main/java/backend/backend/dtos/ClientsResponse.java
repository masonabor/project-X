package backend.backend.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientsResponse {
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String gender;
    private TrainingPlanDTO trainingPlan;

    @Data
    public static class TrainingPlanDTO {
        private List<ScheduleDTO> schedules;
    }
}
