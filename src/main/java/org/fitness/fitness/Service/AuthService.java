package org.fitness.fitness.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpRepository otpRepository;
    public static final String GOOGLE_CLIENT_ID1 = "960263820346-sivuehaobtd2k06esk5co9hjb7cnokft.apps.googleusercontent.com";

    public ResponseEntity<?> register(RegisterRequest request) {
        String Email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Email already registered").build());
        } else if (userRepository.existsByEmailAndIsVerified(Email, false)) {
            var user = userRepository.findByEmail(Email);
            if (otpRepository.existsByEmail(Email)) {
                var otp = otpRepository.findByEmail(Email);
                long secondElapsed = ChronoUnit.SECONDS.between(otp.getCreated(), LocalDateTime.now());
                if (secondElapsed < 30) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Can't send OTP before 30 seconds").build());
                }
                otpRepository.delete(otp);
            }

            user.get().setName(request.getUserName());
            user.get().setPassword(passwordEncoder.encode(Email));
            user.get().setChangePassword(false);
            user.get().setIsVerified(false);
            userRepository.save(user.get());
            String otp = generateotp();
            sendVerificationEmail(Email, otp);
            OTP otp1 = new OTP();
            otp1.setEmail(Email);
            otp1.setOtp(otp);
            otp1.setCreated(LocalDateTime.now());
            otpRepository.save(otp1);
            return ResponseEntity.ok().body(ResponseMessage.builder().message("OTP sent successfully").build());
        } else {
            User user = new User();
            user.setEmail(Email);
            user.setName(request.getUserName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setChangePassword(false);
            user.setIsVerified(false);
            userRepository.save(user);
            String otp = generateotp();
            OTP otp1 = new OTP();
            otp1.setEmail(Email);
            otp1.setOtp(otp);
            otp1.setCreated(LocalDateTime.now());
            otpRepository.save(otp1);
            sendVerificationEmail(Email, otp);
            return ResponseEntity.ok().body(ResponseMessage.builder().message("OTP successfully sent").build());
        }
    }

    private String generateotp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    public void sendVerificationEmail(String email, String otp) {
        String subject = "Verification Mail";
        String imageUrl = "https://i.ibb.co/rbwTxc9/logo-stride.png";
        String body = "<html><body>" + "<img src='" + imageUrl + "' alt='Verification Image' style='max-width:100%;height:auto;'>" + "<p>Your verification code is <strong>" + otp + "</strong></p>" + "</body></html>";

        // Set the content type to HTML
        emailService.sendEmail(email, subject, body, true);
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        String Email = request.getEmail().toLowerCase().trim();
        if (!userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("User not registered").build());
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Email, request.getPassword()));
        } catch (
                BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Incorrect password").build());
        }
        User user = userRepository.findByEmail(Email).get();
        var jwtToken = jwtService.generateToken(user);
        UserDetails userDetails = createUserDetails(user);

        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).message("Login successful").user(userDetails).build());
    }

    public ResponseEntity<?> validate(OtpValidation request) {
        String Email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Account already verified and registered").build());
        }
        if (otpRepository.existsByEmailAndOtp(Email, request.getOtp())) {
            OTP otp = otpRepository.findByEmail(Email);
            long minuteElapsed = ChronoUnit.MINUTES.between(otp.getCreated(), LocalDateTime.now());
            if (minuteElapsed > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("OTP timeout").build());
            }
            User user = userRepository.findByEmail(Email).get();
            user.setIsVerified(true);
            userRepository.save(user);
            otpRepository.delete(otp);
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().message("Account has been registered successfully").user(createUserDetails(user)).token(jwtToken).build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Incorrect Credentials").build());
        }
    }

    public ResponseEntity<?> forgotPassword(String email) {
        String Email = email.toLowerCase().trim();
        if (!userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Account not registered").build());
        }
        OTP otp;
        if (otpRepository.existsByEmail(Email)) {
            otp = otpRepository.findByEmail(Email);
            long secondElapsed;
            if (otp.getCreated() != null)
                secondElapsed = ChronoUnit.SECONDS.between(otp.getCreated(), LocalDateTime.now());
            else
                secondElapsed = 60;
            if (secondElapsed < 30) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("OTP can not be send before 30 second").build());
            }
        } else {
            otp = new OTP();
        }
        String otp1 = generateotp();
        otp.setEmail(Email);
        otp.setOtp(otp1);
        otp.setCreated(LocalDateTime.now());
        otpRepository.save(otp);
        sendVerificationEmail(Email, otp1);
        return ResponseEntity.ok().body(ResponseMessage.builder().message("OTP sent successfully to " + email).build());
    }

    public ResponseEntity<?> resetPassword(ResetPassword request) {
        String Email = request.getEmail().toLowerCase().trim();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        if (!userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Email not registered").build());
        }

        if (!currentUser.getEmail().equals(Email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Token not Valid").build());
        }
        User user = userRepository.findByEmail(Email).get();
        if (user.getChangePassword()) {
            user.setChangePassword(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            redisService.invalidateAllTokens(Email);
            return ResponseEntity.ok().body(ResponseMessage.builder().message("Password Changed Successfully").build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Verify Account First!").build());
        }
    }

    public ResponseEntity<?> emailverify(OtpValidation request) {
        String Email = request.getEmail().toLowerCase().trim();

        if (!userRepository.existsByEmailAndIsVerified(Email, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("Email not registered").build());
        }
        OTP otp = otpRepository.findByEmail(Email);
        long minuteElapsed = ChronoUnit.MINUTES.between(otp.getCreated(), LocalDateTime.now());
        if (minuteElapsed > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("OTP timeout").build());
        }
        if (Objects.equals(otp.getOtp(), request.getOtp())) {
            User user = userRepository.findByEmail(Email).get();
            user.setChangePassword(true);
            userRepository.save(user);
            otpRepository.delete(otp);
            var jwtToken = jwtService.generateToken(user);
            UserDetails userDetails = createUserDetails(user);

            return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).message("Email Verified").user(userDetails).build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.builder().message("invalid OTP").build());
        }
    }

    public UserDetails createUserDetails(User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(user.getName());
        userDetails.setEmail(user.getEmail());
        return userDetails;
    }

    public ResponseEntity<?> ifRegistered(String Email) {
        if (userRepository.existsByEmailAndIsVerified(Email.toLowerCase().trim(), true)) {
            return ResponseEntity.ok().body(ResponseMessage.builder().message("Go to login").build());
        } else {
            return ResponseEntity.ok().body(ResponseMessage.builder().message("Go to SignUP").build());
        }
    }

    @Value("${google.client-id}") private String googleClientId;

    public GoogleIdToken verifyToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory()).setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken token = verifier.verify(idToken);
        if (token != null) {
            GoogleIdToken.Payload payload = token.getPayload();

            // Additional token validation
            if (!payload.getAudience().equals(googleClientId)) {
                throw new IllegalArgumentException("Token audience mismatch");
            }

            return token;
        }

        throw new IllegalArgumentException("Invalid token");
    }

    public ResponseEntity<?> extractUserDetails(GoogleIdToken token) {

        GoogleIdToken.Payload payload = token.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String message = "Login successful";
        if (!userRepository.existsByEmailAndIsVerified(email, true)) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setIsVerified(true);
            user.setPassword("OAuth_USER");
            userRepository.save(user);
            message = "Account created successful";
        }
        User user = userRepository.findByEmail(email).get();
        var jwtToken = jwtService.generateToken(user);
        System.out.println("Google Token Payload: " + payload);

        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).message(message).user(createUserDetails(user)).build());
    }
}
