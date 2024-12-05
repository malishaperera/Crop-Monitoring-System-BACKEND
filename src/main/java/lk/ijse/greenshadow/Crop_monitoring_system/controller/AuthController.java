package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.JwtAuthResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.SignIn;
import lk.ijse.greenshadow.Crop_monitoring_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "signup",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JwtAuthResponse> signUp(
            @RequestPart ("email") String email,
            @RequestPart ("password") String password,
            @RequestPart ("staffMemberId") String staffMemberId) {
        try {

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


    @PostMapping(value = "signin")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignIn signIn) {
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken (@RequestParam ("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }
}
