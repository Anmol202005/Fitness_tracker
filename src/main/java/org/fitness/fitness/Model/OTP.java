package org.fitness.fitness.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name ="otp")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long otpId;
    private String otp;
    private String email;
    private LocalDateTime created;
}
