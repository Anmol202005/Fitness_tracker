package org.fitness.fitness.Model.DTO;

public record GoogleSignRequest (
        String email,
        String name,
        String given_name,
        String family_name,
        String iss,
        String azp,
        Long exp
){}
