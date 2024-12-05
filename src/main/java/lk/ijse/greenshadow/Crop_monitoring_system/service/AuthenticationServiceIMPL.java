package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.UserEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.JwtAuthResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.SignIn;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.UserRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceIMPL implements AuthenticationService {


    private final UserRepository userDao;
    private final JWTService jwtService;
    private final Mapping mapping;
    private final StaffRepository staffDao;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        var userByEmail = userDao.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var generatedToken = jwtService.generateToken(userByEmail);
        return JwtAuthResponse.builder().token(generatedToken).build() ;
    }

    @Override
    public JwtAuthResponse signUp(UserDTO signUpUser) {
        // Validate and fetch the staff member
        var staffMember = staffDao.findById(signUpUser.getStaffMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff member ID"));

        // Build UserEntity with staff association and role from StaffEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(signUpUser.getEmail());
        userEntity.setPassword(signUpUser.getPassword());
        userEntity.setRole(staffMember.getRole()); // Assign role from staff
        userEntity.setStaff(staffMember);

        // Save the user
        var savedUser = userDao.save(userEntity);

        // Generate JWT token
        var genToken = jwtService.generateToken(savedUser);

        return JwtAuthResponse.builder().token(genToken).build();
    }

    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        var userName = jwtService.extractUsername(accessToken);
        var userEntity =
                userDao.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(userEntity);
        return JwtAuthResponse.builder().token(refreshToken).build();
    }
}