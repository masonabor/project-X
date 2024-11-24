package backend.backend.jwt;

import lombok.Data;

import java.util.Date;

@Data
public class JwtError {

    private int status;
    private String message;
    private Date timestamp;

    public JwtError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
