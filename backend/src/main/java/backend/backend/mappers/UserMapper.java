package backend.backend.mappers;

import backend.backend.dtos.UserGetResponse;
import backend.backend.models.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {

    public static UserGetResponse toDto(User user) {
        if (user == null) {
            return null;
        }

        UserGetResponse dto = new UserGetResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMiddleName(user.getMiddleName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        dto.setAccountNumber(user.getAccount() == null ? 0 : user.getAccount().getAccountNumber());
        dto.setCoachName(user.getTrainingPlan() == null ? "" : user.getTrainingPlan().getCoach().getFirstName());
        dto.setCoachLastName(user.getTrainingPlan() == null ? "" : user.getTrainingPlan().getCoach().getLastName());
        dto.setCoachPhone(user.getTrainingPlan() == null ? "" : user.getTrainingPlan().getCoach().getPhone());
        dto.setBanned(user.isBanned());
        dto.setRole(user.getRole().getRole());

        return dto;
    }
}
