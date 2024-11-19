package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.CropDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CropServiceImpl implements CropService {

    private final CropDao cropDao;



    @Autowired
    private final Mapping mapping;

    private final FieldDao fieldDao;

    @Override
    public void saveCrop(CropDTO cropDTO) {
        List<String> cropCode = cropDao.findLastCropCode();
        String lastCropCode = cropCode.isEmpty() ? null : cropCode.get(0);
        cropDTO.setCropCode(AppUtil.generateNextCropId(lastCropCode));

        if (cropDao.existsById(cropDTO.getCropCode())) {
            throw new DataPersistFailedException("Crop not saved: Crop with the same code already exists");
        }

        FieldEntity field = fieldDao.findByFieldCode(cropDTO.getFieldCode());
        if (field == null) {
            throw new DataPersistFailedException("Field not found for the provided fieldCode");
        }


        CropEntity isSaveCrop = cropDao.save(mapping.convertToCropEntity(cropDTO));
        if (isSaveCrop == null) {
            throw new DataPersistFailedException("Crop not saved: Unable to persist CropEntity");
        }
    }

    @Override
    public void updateCrop(CropDTO updateCrop, String cropCode) {
        // Check if the CropEntity exists
        Optional<CropEntity> existingCropTmp = cropDao.findById(cropCode);
        if (existingCropTmp.isEmpty()) {
            throw new CropNotFoundException("Crop with code " + cropCode + " not found");
        }

        // Validate and fetch FieldEntity using fieldCode
        FieldEntity field = fieldDao.findByFieldCode(updateCrop.getFieldCode());
        if (field == null) {
            throw new DataPersistFailedException("Field not found for the provided fieldCode");
        }

        CropEntity existingCrop = existingCropTmp.get();

        // Update the CropEntity with new data
        existingCrop.setCropCommonName(updateCrop.getCropCommonName());
        existingCrop.setCropScientificName(updateCrop.getCropScientificName());
        existingCrop.setCropImage(updateCrop.getCropImage());
        existingCrop.setCategory(updateCrop.getCategory());
        existingCrop.setCropSeason(updateCrop.getCropSeason());
        existingCrop.setField(field);  // Set the updated field entity

        // Save the updated entity
        cropDao.save(existingCrop);
    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> selectedCropId = cropDao.findById(cropCode);
        if (!selectedCropId.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        } else {
            cropDao.deleteById(cropCode);
        }
    }

    @Override
    public CropResponse getSelectCrop(String cropCode) {
        if (cropDao.existsById(cropCode)) {
            CropEntity  cropEntityByCropCode = cropDao.getCropEntityByCropCode(cropCode);
            return mapping.convertToCropDTO(cropEntityByCropCode);
        }else {
            return new CropErrorResponse(0,"Crop not found");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        List<CropEntity> getAllCrops = cropDao.findAll();
        return mapping.convertToCropDTOList(getAllCrops);
    }
}