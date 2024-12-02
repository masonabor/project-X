package backend.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoachDTO {
    private Long id;
    private String lastName;
    private String firstName;
}
