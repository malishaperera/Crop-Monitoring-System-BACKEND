package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.StaffNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
@Slf4j
public class StaffController {

    @Autowired
    private final StaffService staffService;

    @GetMapping("/health")
    public String healthCheck(){
        return "Staff is running";
    }


    /**To Do CRUD Operation**/

    //Save Staff
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveStaff(@RequestBody StaffDTO staffDTO){
        log.info("Received request to save staff with data: {}", staffDTO);
        if (staffDTO == null) {
            log.error("Staff data is null in the request.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff cannot be null");
        }else {
            try {
                staffService.saveStaff(staffDTO);
                log.info("Staff saved successfully with data: {}", staffDTO);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }catch (DataPersistFailedException e){
                log.error("Data persist failed while saving staff: {}", staffDTO, e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                log.error("Unexpected error occurred while saving staff: {}", staffDTO, e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    //Update Staff
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{staffMemberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStaff(@PathVariable("staffMemberId") String staffMemberId , @RequestBody StaffDTO staff){
        log.info("Received request to update staff with ID: {} and data: {}", staffMemberId, staff);
        try {
            if (staff == null && (staffMemberId == null || staff.equals(""))){
                log.error("Staff data or staff member ID is invalid.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.updateStaff(staffMemberId,staff);
            log.info("Staff with ID: {} updated successfully.", staffMemberId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (StaffNotFoundException e){
            log.error("Staff with ID: {} not found for update", staffMemberId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Unexpected error occurred while updating staff with ID: {}", staffMemberId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Staff
    @DeleteMapping(value = "/{staffMemberId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable("staffMemberId") String staffMemberId){
        log.info("Received request to delete staff with ID: {}", staffMemberId);
        try {
            staffService.deleteStaff(staffMemberId);
            log.info("Staff with ID: {} deleted successfully.", staffMemberId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            log.error("Staff with ID: {} not found for deletion", staffMemberId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Unexpected error occurred while deleting staff with ID: {}", staffMemberId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Staff
    @GetMapping(value = "/{staffMemberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffResponse getSelectStaff(@PathVariable("staffMemberId") String staffMemberId){
        log.info("Received request to get staff with ID: {}", staffMemberId);
        return staffService.getSelectStaff(staffMemberId);
    }

    //Get All Staff
    @GetMapping(value = "allStaffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getAllStaffs(){
        return staffService.getAllStaffs();
    }
}