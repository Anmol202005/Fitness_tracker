package org.fitness.fitness.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPassword {
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character")
    private String password;
     @Email(message = "invalid Email")
    private String email;

}
