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
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Repository.UserRepository;
import org.fitness.fitness.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication, registration, password management, etc.")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Operation(summary = "User Registration", description = "Registers a new user in the system.")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ){
        return authService.register(request);
    }

    @Operation(summary = "User Authentication", description = "Authenticates the user with email and password.")
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
            ){
        return authService.authenticate(request);
    }

    @Operation(summary = "Validate OTP", description = "Validates the OTP for actions like account verification.")
    @PostMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestBody OtpValidation request
            ){
        return authService.validate(request);
    }

    @Operation(summary = "Forgot Password", description = "Initiates the process to reset the user's password by sending an OTP to the email.")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
            ) {
        return authService.forgotPassword(request.getEmail());
    }

    @Operation(summary = "Verify Email OTP", description = "Verifies the OTP sent to the user's email address.")
    @PostMapping("/verify-Email")
    public ResponseEntity<?> verifyEmail(
            @RequestBody OtpValidation request
            ){
        return authService.emailverify(request);
    }

    @Operation(summary = "Reset Password", description = "Resets the user's password after OTP validation.")
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
          @Valid @RequestBody ResetPassword request
    ){
        try {
        return authService.resetPassword(request);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.builder()
                                 .message(e.getMessage())
                                 .build());
    }
    }

    @Operation(summary = "OAuth Success", description = "Handles the successful OAuth token exchange.")
    @PostMapping("oauth/success")
    public ResponseEntity<?> success(@RequestBody OAuthToken request) throws IOException, InterruptedException {
        return authService.success(request.getToken());
    }

    @Operation(summary = "Check if Email is Registered", description = "Checks if the provided email is already registered in the system.")
    @PostMapping("/ifRegistered")
    public ResponseEntity<?> ifRegistered(@RequestBody ForgotPasswordRequest request){
        return authService.ifRegistered(request.getEmail());
    }
}
