package org.fitness.fitness.Controller;

import java.io.IOException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.AuthenticationRequest;
import org.fitness.fitness.Model.DTO.ForgotPasswordRequest;
import org.fitness.fitness.Model.DTO.OAuthToken;
import org.fitness.fitness.Model.DTO.OtpValidation;
import org.fitness.fitness.Model.DTO.RegisterRequest;
import org.fitness.fitness.Model.DTO.ResetPassword;
import org.fitness.fitness.Repository.UserRepository;
import org.fitness.fitness.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ){
        return authService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
            ){
        return authService.authenticate(request);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestBody OtpValidation request
            ){
        return authService.validate(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
            ) {
        return authService.forgotPassword(request.getEmail());
    }

    @PostMapping("/verify-Email")
    public ResponseEntity<?> verifyEmail(
            @RequestBody OtpValidation request
            ){
        return authService.emailverify(request);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
          @Valid @RequestBody ResetPassword request
    ){
        return authService.resetPassword(request);
    }

    @PostMapping("oauth/success")
    public ResponseEntity<?> success(@RequestBody OAuthToken request) throws IOException, InterruptedException {
        return authService.success(request.getToken());
    }

    @PostMapping("/ifRegistered")
    public ResponseEntity<?> ifRegistered(ForgotPasswordRequest request){
        return authService.ifRegistered(request.getEmail());
    }

}
