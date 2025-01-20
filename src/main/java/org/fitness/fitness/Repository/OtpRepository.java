package org.fitness.fitness.Repository;

import org.fitness.fitness.Model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Long> {
 Boolean existsByEmailAndOtp(String email, String otp);
 OTP findByEmail(String email);
 Boolean existsByEmail(String email);
}
