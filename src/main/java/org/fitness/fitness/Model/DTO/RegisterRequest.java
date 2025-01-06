package org.fitness.fitness.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
     @NotNull(message = "User Name can't be empty")
    @Size(min = 1,max=12,message = "User can't be more than 12 character")
     @Pattern(regexp = "^[a-zA-Z]*$", message = "User Name can't contain spaces or numbers")
    private String userName;
    @Email(message = "invalid Email")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character")
    private String password;
}
