package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.UserEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.JwtAuthResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.SignIn;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.UserRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthenticationServiceIMPL implements AuthenticationService {


    private final UserRepository userDao;
    private final JWTService jwtService;
    private final Mapping mapping;
    private final StaffRepository staffDao;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        log.info("Attempting sign-in for email: {}", signIn.getEmail());

        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));

            // Fetch user by email
            var userByEmail = userDao.findByEmail(signIn.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate JWT token
            var generatedToken = jwtService.generateToken(userByEmail);
            log.info("Generated JWT Token: {}", generatedToken);

            log.info("Sign-in successful for email: {}", signIn.getEmail());
            return JwtAuthResponse.builder().token(generatedToken).build();
        } catch (UsernameNotFoundException e) {
            log.error("Sign-in failed for email: {}. User not found", signIn.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during sign-in for email: {}", signIn.getEmail(), e);
            throw new RuntimeException("Sign-in failed due to unexpected error", e);
        }
    }

    @Override
    public JwtAuthResponse signUp(UserDTO signUpUser) {
        log.info("Starting sign-up process for email: {}", signUpUser.getEmail());

        try {
            // Validate and fetch the staff member
            var staffMember = staffDao.findById(signUpUser.getStaffMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid staff member ID"));

            // Build UserEntity with staff association and role from StaffEntity
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(signUpUser.getEmail());
            userEntity.setPassword(signUpUser.getPassword());
            userEntity.setRole(staffMember.getRole());  // Assign role from staff
            userEntity.setStaff(staffMember);

            // Save the user
            var savedUser = userDao.save(userEntity);

            // Generate JWT token
            var genToken = jwtService.generateToken(savedUser);

            log.info("Sign-up successful for email: {}", signUpUser.getEmail());
            return JwtAuthResponse.builder().token(genToken).build();
        } catch (IllegalArgumentException e) {
            log.error("Sign-up failed for email: {}. Invalid staff member ID", signUpUser.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during sign-up for email: {}", signUpUser.getEmail(), e);
            throw new RuntimeException("Sign-up failed due to unexpected error", e);
        }
    }

    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        log.info("Attempting to refresh token for access token: {}", accessToken);

        try {
            var userName = jwtService.extractUsername(accessToken);
            var userEntity = userDao.findByEmail(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate refresh token
            var refreshToken = jwtService.refreshToken(userEntity);

            log.info("Token refresh successful for email: {}", userName);
            return JwtAuthResponse.builder().token(refreshToken).build();
        } catch (UsernameNotFoundException e) {
            log.error("Token refresh failed. User not found for token: {}", accessToken);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token refresh for token: {}", accessToken, e);
            throw new RuntimeException("Token refresh failed due to unexpected error", e);
        }
    }
}