package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;

import java.util.List;

public interface CropService {
    void saveCrop(CropDTO buildCropDTO,String fieldCode);

    void updateCrop(CropDTO updateCrop,String fieldCode);

    void deleteCrop(String cropCode);

    CropResponse getSelectCrop(String cropCode);

    List<CropDTO> getAllCrops();
}
