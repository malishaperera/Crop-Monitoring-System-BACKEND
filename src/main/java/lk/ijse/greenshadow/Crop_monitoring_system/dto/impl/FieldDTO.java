package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.EquipmentEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldStaffDetailsEntity;
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

    private List<CropDTO> cropDTOList = new ArrayList<>();
    private List<EquipmentDTO> equipmentDTOList = new ArrayList<>();

    //Associate
    // Associate with FieldStaffDetailsEntity
    private List<FieldStaffDetailsEntity> fieldStaffDetailsList = new ArrayList<>();
    private List<FieldLogDetailsEntity> fieldLogDetailsList = new ArrayList<>();
}