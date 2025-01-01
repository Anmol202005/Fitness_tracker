package org.fitness.fitness.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String message;
    private UserDetails user;
}
