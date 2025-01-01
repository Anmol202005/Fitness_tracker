package org.fitness.fitness.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.AuthenticationRequest;
import org.fitness.fitness.Model.DTO.AuthenticationResponse;
import org.fitness.fitness.Model.DTO.OtpValidation;
import org.fitness.fitness.Model.DTO.RegisterRequest;
import org.fitness.fitness.Model.DTO.ResetPassword;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.DTO.UserDetails;
import org.fitness.fitness.Model.OTP;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.OtpRepository;
import org.fitness.fitness.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpRepository otpRepository;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByEmailAndIsVerified(request.getEmail(), true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Email already registered")
                    .build());
        } else if (userRepository.existsByEmailAndIsVerified(request.getEmail(), false)) {
            var user = userRepository.findByEmail(request.getEmail());
            if (userRepository.existsByUsername(request.getUserName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("Username already in use")
                        .build());
            }
            user.get().setUsername(request.getUserName());
            user.get().setPassword(passwordEncoder.encode(request.getPassword()));
            user.get().setChangePassword(false);
            userRepository.save(user.get());
            String otp = generateotp();
            sendVerificationEmail(user.get().getEmail(), otp);
            OTP otp1 = new OTP();
            otp1.setEmail(user.get().getEmail());
            otp1.setOtp(otp);
            otp1.setCreated(LocalDateTime.now());
            return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("User successfully registered")
                    .build());
        } else {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUserName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setChangePassword(false);
            userRepository.save(user);
            String otp = generateotp();
            OTP otp1 = new OTP();
            otp1.setEmail(request.getEmail());
            otp1.setOtp(otp);
            otp1.setCreated(LocalDateTime.now());
            sendVerificationEmail(request.getEmail(), otp);
            return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("User successfully registered")
                    .build());
        }
    }

    private String generateotp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendVerificationEmail(String email, String otp) {
        String subject = "Verification Mail";
        String imageUrl = "https://i.ibb.co/kJkpyt6/Workify.png";
        String body = "<html><body>" +
                "<img src='" + imageUrl + "' alt='Verification Image' style='max-width:100%;height:auto;'>" +
                "<p>Your verification code is <strong>" + otp + "</strong></p>" +
                "</body></html>";

        // Set the content type to HTML
        emailService.sendEmail(email, subject, body, true);
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        if (!userRepository.existsByEmailAndIsVerified(request.getEmail(), true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder()
                                                                                     .message("User not registered")
                                                                                     .build());
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(Locale.ROOT),
                            request.getPassword()));
        } catch (
                BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Incorrect password")
                    .build());
        }
        User user = userRepository.findByEmail(request.getEmail()).get();
        var jwtToken = jwtService.generateToken(user);
        UserDetails userDetails = createUserDetails(user);

        return ResponseEntity.ok(AuthenticationResponse
                .builder()
                .token(jwtToken)
                .message("Login successful")
                .user(userDetails)
                .build());
    }

    public ResponseEntity<?> validate(OtpValidation request) {
        if (otpRepository.existsByEmailAndOtp(request.getEmail(), request.getOtp())) {
            OTP otp = otpRepository.findByEmail(request.getEmail());
            long minuteElapsed = ChronoUnit.MINUTES.between(otp.getCreated(), LocalDateTime.now());
            if (minuteElapsed > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("OTP timeout")
                        .build());
            }
            User user = userRepository.findByEmail(request.getEmail()).get();
            user.setIsVerified(true);
            userRepository.save(user);
            otp.setCreated(null);
            otpRepository.save(otp);
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder()
                                                           .message("Account has been registered successfully")
                                                           .user(createUserDetails(user))
                                                           .token(jwtToken)
                                                           .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Account not registered")
                    .build());
        }
    }

    public ResponseEntity<?> forgotPassword(String email) {
        if (!userRepository.existsByEmailAndIsVerified(email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Account not registered")
                    .build());
        }
        OTP otp = otpRepository.findByEmail(email);
        long secondElapsed;
        if (otp.getCreated() != null)
            secondElapsed = ChronoUnit.SECONDS.between(otp.getCreated(), LocalDateTime.now());
        else
            secondElapsed = 60;
        if (secondElapsed < 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder()
                                                                                     .message("OTP can not be send before 30 second")
                                                                                     .build());
        }
        String otp1 = generateotp();
        otp.setOtp(otp1);
        otp.setCreated(LocalDateTime.now());
        otpRepository.save(otp);
        sendVerificationEmail(email, otp1);
        return ResponseEntity.ok().body(ResponseMessage
                .builder()
                .message("OTP sent successfully to" + email)
                .build());
    }

    public ResponseEntity<?> resetPassword(ResetPassword request) {
        if (!userRepository.existsByEmailAndIsVerified(request.getEmail(), true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder()
                                                                                     .message("Email not registered")
                                                                                     .build());
        }
        User user = userRepository.findByEmail(request.getEmail()).get();
        if (user.getChangePassword()) {
            user.setChangePassword(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("Password Changed Successfully")
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder()
                                                                                     .message("Verify Account First!")
                                                                                     .build());
        }
    }

    public ResponseEntity<?> emailverify(OtpValidation request) {
        if (!userRepository.existsByEmailAndIsVerified(request.getEmail(), true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder()
                                                                                     .message("Email not registered")
                                                                                     .build());
        }
        OTP otp = otpRepository.findByEmail(request.getEmail());
        long minuteElapsed = ChronoUnit.MINUTES.between(otp.getCreated(), LocalDateTime.now());
        if (minuteElapsed > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("OTP timeout")
                    .build());
        }
        User user = userRepository.findByEmail(request.getEmail()).get();
        user.setChangePassword(true);
        userRepository.save(user);
        return ResponseEntity.ok().body(ResponseMessage
                .builder()
                .message("OTP sent successfully to" + request.getEmail())
                .build());
    }
    public UserDetails createUserDetails(User user){
         UserDetails userDetails = new UserDetails();
        userDetails.setUsername(user.getUsername());
        userDetails.setEmail(user.getEmail());
        return userDetails;
    }
}
