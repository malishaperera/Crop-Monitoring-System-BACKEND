package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Gender;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO implements SuperDTO, StaffResponse {
    private String staffMemberId;
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Designation cannot be null")
    private String designation;
    private Gender gender;
    private Date joinedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date DOB;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a valid 10 digit number")
    private String contactNo;
    @Email(message = "Email should be valid")
    private String email;
    private Role role;

    //Another
    private String equipmentId;
    private List<String> vehicleCodes = new ArrayList<>();

    private List<String> fieldCode =  new ArrayList<>();
    private List<String> logCodes = new ArrayList<>();
}