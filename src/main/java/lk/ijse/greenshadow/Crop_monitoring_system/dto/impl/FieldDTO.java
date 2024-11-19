package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO implements SuperDTO, FieldResponse {
    private String fieldCode;
    private String fieldName;
    private Point fieldLocation;
    private double fieldSize;
    private String fieldImage1;
    private String fieldImage2;

    private List<String> cropCodes = new ArrayList<>();
    private List<String> equipmentIds = new ArrayList<>();

    //Associate
    private List<String> staffMemberIds = new ArrayList<>();
    private List<String> logCodes = new ArrayList<>();
}