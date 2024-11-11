package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropDTO implements SuperDTO, CropResponse {
    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    private String cropImage;
    private String category;
    private String cropSeason;
    private FieldEntity field;
}