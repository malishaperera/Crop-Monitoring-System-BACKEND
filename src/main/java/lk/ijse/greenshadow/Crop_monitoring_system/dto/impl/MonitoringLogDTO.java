package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


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
    private String logObservation;
    private String observedImage;

    //Associate
    private List<String> fieldCodes = new ArrayList<>();
    private List<String> cropCodes = new ArrayList<>();
    private List<String> staffMemberIds = new ArrayList<>();

}