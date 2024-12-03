package lk.ijse.greenshadow.Crop_monitoring_system.controller;


import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.UserDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/health")
    public String healthCheck() {
        return "User is running";
    }


    //Save User
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO){
        log.info("Attempting to save user with data: {}", userDTO);

        try {
            // Call the service to save the equipment
            userService.saveUser(userDTO);
            log.info("User saved successfully with email: {}", userDTO.getEmail());
            return new ResponseEntity<>("User saved successfully", HttpStatus.CREATED);

        } catch (DataPersistFailedException e) {
            log.error("User already exists with email: {}", userDTO.getEmail());
            return new ResponseEntity<>("User member already exist: " , HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error occurred while saving user with email: {}", userDTO.getEmail(), e);
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update User
    @PatchMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody UserDTO userDTO) {
        log.info("Attempting to update user with email: {} and data: {}", email, userDTO);

        try {
            // Call the service to update the user by email
            userService.updateUser(email, userDTO);
            log.info("User updated successfully with email: {}", email);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);

        } catch (DataPersistFailedException e) {
            log.error("User update failed for email: {} due to: {}", email, e.getMessage());
            return new ResponseEntity<>("User update failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user with email: {}", email, e);
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete User
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        log.info("Attempting to delete user with email: {}", email);

        try {
            userService.deleteUser(email);
            log.info("User deleted successfully with email: {}", email);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            log.error("User not found for deletion with email: {}", email);
            return new ResponseEntity<>("User not found: " + email, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting user with email: {}", email, e);
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email) {
        log.info("Attempting to fetch user with email: {}", email);

        try {
            UserDTO userDTO = userService.getUserByEmail(email);
            log.info("User details fetched successfully for email: {}", email);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            log.error("User not found for email: {}", email);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching user with email: {}", email, e);

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}