package backend.backend.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CoachResponse {
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String gender;
    private String role;
}
