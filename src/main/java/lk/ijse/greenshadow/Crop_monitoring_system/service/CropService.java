package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;

import java.util.List;

public interface CropService {
    void saveCrop(CropDTO buildCropDTO);

    void updateCrop(CropDTO updateCrop);

    void deleteCrop(String cropCode);

    CropResponse getSelectCrop(String cropCode);

    List<CropDTO> getAllCrops();
}