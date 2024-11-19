package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String firstName;
    private String lastName;
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
    private String contactNo;
    private String email;
    private Role role;

    //Another
    private String equipmentId;
    private List<String> vehicleCodes = new ArrayList<>();


    //Associate
//    private List<FieldStaffDetailsEntity> fieldStaffDetailsList = new ArrayList<>();
//    private List<StaffLogDetailsEntity> staffLogDetailsList = new ArrayList<>();
    private List<String> fieldCode =  new ArrayList<>();
    private List<String> logCodes = new ArrayList<>();

}