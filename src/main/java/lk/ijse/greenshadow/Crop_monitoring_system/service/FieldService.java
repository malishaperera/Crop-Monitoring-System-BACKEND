package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;


import java.util.List;

public interface FieldService {
    void saveField(FieldDTO buildFieldDTO);

    void updateField(FieldDTO updateField);

    void deleteField(String fieldCode);

    FieldResponse getSelectField(String fieldCode);

    List<FieldDTO> getAllFields();


    //Custom
//    FieldEntity existByField(String fieldCode);
}
