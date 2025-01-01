package org.fitness.fitness.Repository;

import java.util.UUID;

import org.fitness.fitness.Model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OTP, UUID> {
 Boolean existsByEmailAndOtp(String email, String otp);
 OTP findByEmail(String email);
}
