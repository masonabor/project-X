package backend.backend.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String gender;
    private RoleDTO role;
    private AccountDTO account;

    @Data
    public static class RoleDTO {
        private String role;
    }

    @Data
    public static class AccountDTO {
        private long accountNumber;
    }
}