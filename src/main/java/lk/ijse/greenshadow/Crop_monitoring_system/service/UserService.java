package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;

public interface UserService {
    void saveUser(UserDTO userDTO);

    void updateUser(String email, UserDTO userDTO);

    void deleteUser(String email);

    UserDTO getUserByEmail(String email);
}
