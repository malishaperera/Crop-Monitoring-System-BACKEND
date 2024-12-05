package lk.ijse.greenshadow.Crop_monitoring_system.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


public interface JWTService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String refreshToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);

}
