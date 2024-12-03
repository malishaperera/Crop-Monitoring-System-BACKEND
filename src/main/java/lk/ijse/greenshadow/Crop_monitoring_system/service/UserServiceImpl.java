package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.UserEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public void saveUser(UserDTO userDTO) {

        Optional<UserEntity> existingUser = userRepository.findByEmail(userDTO.getEmail());

        if (existingUser.isPresent()) {
            // If the user exists, throw an exception or handle it
            throw new DataPersistFailedException("User with email " + userDTO.getEmail() + " already exists.");
        }

        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        userRepository.save(user);


    }

    @Override
    public void updateUser(String email, UserDTO userDTO) {
        // Fetch the existing user by email
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();

            // Only update the fields that can change (password and role)
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());

            // Save the updated user back to the database
            userRepository.save(user);
        } else {
            throw new DataPersistFailedException("User not found with email: " + email);
        }
    }

    @Override
    public void deleteUser(String email) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
        } else {
            throw new DataPersistFailedException("User not found with email: " + email);
        }
    }


    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            UserEntity userEntity = existingUser.get();
            return new UserDTO(userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
        } else {
            throw new DataPersistFailedException("User not found with email: " + email);
        }
    }

}
