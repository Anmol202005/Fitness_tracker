package org.fitness.fitness.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OAuthDetails {
    private String email;
    private String name;
    private String pictureUrl;

}
