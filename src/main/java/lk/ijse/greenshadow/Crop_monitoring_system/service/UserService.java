package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.UserResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);
    void deleteUser(String email);
    UserDTO getUserByEmail(String email);
    //Security
    UserDetailsService userDetailsService();

    List<UserDTO> getAllUsers();
    UserResponse getSelectedUser(String email);
    void updateUser(UserDTO updateUser);
}