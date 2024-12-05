package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.UserErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.UserResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.UserEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.UserNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.UserRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Autowired
    private final Mapping mapping;

        @Override
        public void saveUser(UserDTO userDTO) {

            UserEntity savedUser =  userRepository.save(mapping.convertToUserEntity(userDTO));
            if(savedUser == null && savedUser.getEmail() == null ) {
                throw new DataPersistFailedException("Cannot data saved");
            }
        }


    @Override
    public void updateUser(UserDTO userDTO) {
        // Retrieve the user entity by email
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userDTO.getEmail());
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + userDTO.getEmail());
        }

        UserEntity userEntity = optionalUserEntity.get();

        // Update the user's role if it has changed
        if (userDTO.getRole() != null && !userDTO.getRole().equals(userEntity.getRole())) {
            userEntity.setRole(userDTO.getRole());

            // Update the role in StaffEntity if the user is linked to a staff member
            if (userEntity.getStaff() != null) {
                staffRepository.updateRoleByStaffMemberId(userDTO.getRole().name(), userDTO.getStaffMemberId());
            }
        }

        // Update the user's password if a new one is provided (without encoding)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userEntity.setPassword(userDTO.getPassword());  // Save the password as-is (without encoding)
        }

        // Save the updated user entity
        userRepository.save(userEntity);
    }


    @Override
    public void deleteUser(String email) {
        Optional<UserEntity> selectedUserEmail = userRepository.findById(email);
        if(!selectedUserEmail.isPresent()) {
            throw new UserNotFoundException("User not found");
        }else {
            userRepository.deleteById(email);
        }
    }

    @Override
    public UserResponse getSelectedUser(String email) {
        if(userRepository.existsById(email)){
            UserEntity userEntityByUserEmail = userRepository.getReferenceById(email);
            return (UserResponse) mapping.convertToUserDTO(userEntityByUserEmail);
        }else {
            return new UserErrorResponse(0,"User not found");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return mapping.convertUserToDTOList(userRepository.findAll());
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email ->
                userRepository.findByEmail(email)
                        .orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return null;
    }
}



