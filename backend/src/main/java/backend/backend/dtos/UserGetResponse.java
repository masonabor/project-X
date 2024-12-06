package backend.backend.dtos;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class UserGetResponse {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String gender;
    private boolean banned;
    private long accountNumber;
    private String coachName;
    private String coachLastName;
    private String coachPhone;
    private List<ScheduleDTO> schedules;
    private String role;
}
