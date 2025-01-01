package org.fitness.fitness.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
     @NotNull(message = "First Name can't be empty")
    @Size(min = 1,message = "first name can't be empty")
    private String userName;
    @Email(message = "invalid Email")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character")
    private String password;
}
