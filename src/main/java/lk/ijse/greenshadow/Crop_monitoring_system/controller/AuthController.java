package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.JwtAuthResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.SignIn;
import lk.ijse.greenshadow.Crop_monitoring_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "signup",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JwtAuthResponse> signUp(
            @RequestPart ("email") String email,
            @RequestPart ("password") String password,
            @RequestPart ("staffMemberId") String staffMemberId) {
        try {

            System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+email);



            // build the user
            UserDTO buildUserDTO = new UserDTO();
            buildUserDTO.setEmail(email);
            buildUserDTO.setStaffMemberId(staffMemberId);
            buildUserDTO.setPassword(passwordEncoder.encode(password));
            //send to the service layer
            return ResponseEntity.ok(authenticationService.signUp(buildUserDTO));
        }catch (DataPersistFailedException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<JwtAuthResponse> signUp(@RequestBody UserDTO userDTO) {
//        log.info("Starting signup process for email: {}", userDTO.getEmail());
//        try {
//            // The userDTO is now directly mapped from JSON data sent by the frontend
//            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encrypt password
//            log.info("Password encrypted for user: {}", userDTO.getEmail());
//
//            // Send to the service layer
//            return ResponseEntity.ok(authenticationService.signUp(userDTO));
//        } catch (DataPersistFailedException e) {
//            log.error("Error during signup for email: {}: {}", userDTO.getEmail(), e.getMessage());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("Unexpected error during signup for email: {}: {}", userDTO.getEmail(), e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping(value = "signin")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignIn signIn) {
        log.info("User attempting to sign in with email: {}", signIn.getEmail());
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken (@RequestParam ("refreshToken") String refreshToken) {
        log.info("Refreshing token for refresh token: {}", refreshToken);
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }
}
