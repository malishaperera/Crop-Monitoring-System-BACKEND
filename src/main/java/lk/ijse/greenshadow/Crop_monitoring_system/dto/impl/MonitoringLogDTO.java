package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringLogDTO implements SuperDTO, MonitoringLogResponse {
    private String logCode;

    private Date logDate;
    @NotBlank(message = "Observation is mandatory.")
    private String logObservation;
    private String observedImage;

    //Associate
    @NotNull(message = "Field codes are required.")
    private List<String> fieldCodes = new ArrayList<>();
    @NotNull(message = "Crop codes are required.")
    private List<String> cropCodes = new ArrayList<>();
    @NotNull(message = "Staff member IDs are required.")
    private List<String> staffMemberIds = new ArrayList<>();
}