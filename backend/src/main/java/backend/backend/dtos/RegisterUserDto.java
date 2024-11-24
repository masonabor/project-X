package backend.backend.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterUserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private String gender;
    private Date dateOfBirth;

}
