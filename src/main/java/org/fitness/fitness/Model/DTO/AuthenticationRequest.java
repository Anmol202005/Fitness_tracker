package org.fitness.fitness.Model.DTO;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}

