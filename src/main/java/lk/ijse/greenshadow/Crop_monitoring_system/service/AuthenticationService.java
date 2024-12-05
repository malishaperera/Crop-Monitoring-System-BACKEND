package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.JwtAuthResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.jwtModels.SignIn;

public interface AuthenticationService {
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse signUp(UserDTO signUp);
    JwtAuthResponse refreshToken(String accessToken);
}
