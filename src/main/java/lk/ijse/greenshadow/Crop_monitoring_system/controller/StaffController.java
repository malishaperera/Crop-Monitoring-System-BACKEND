package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.StaffNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.StaffService;
import lombok.RequiredArgsConstructor;
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
        if (staffDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff cannot be null");
        }else {
            try {
                staffService.saveStaff(staffDTO);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }catch (DataPersistFailedException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }



    //Update Staff
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{staffMemberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStaff(@PathVariable("staffMemberId") String staffMemberId , @RequestBody StaffDTO staff){
        try {
            if (staff == null && (staffMemberId == null || staff.equals(""))){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.updateStaff(staffMemberId,staff);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (StaffNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Staff
    @DeleteMapping(value = "/{staffMemberId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable("staffMemberId") String staffMemberId){
        try {
            staffService.deleteStaff(staffMemberId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Staff
    @GetMapping(value = "/{staffMemberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffResponse getSelectStaff(@PathVariable("staffMemberId") String staffMemberId){
        return staffService.getSelectStaff(staffMemberId);
    }

    //Get All Staff
    @GetMapping(value = "allStaffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getAllStaffs(){
        return staffService.getAllStaffs();
    }
}