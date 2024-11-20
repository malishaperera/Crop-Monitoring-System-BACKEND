package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.CropRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.FieldRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
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

    private final CropRepository cropRepository;


    private final Mapping mapping;

    private final FieldRepository fieldDao;


    @Override
    public void saveCrop(CropDTO cropDTO) {
        List<String> cropCode = cropRepository.findLastCropCode();
        String lastCropCode = cropCode.isEmpty() ? null : cropCode.get(0);
        cropDTO.setCropCode(AppUtil.generateNextCropId(lastCropCode));

        if (cropRepository.existsById(cropDTO.getCropCode())) {
            throw new DataPersistFailedException("Crop not saved: Crop with the same code already exists");
        }

        FieldEntity field = fieldDao.findByFieldCode(cropDTO.getFieldCode());
        if (field == null) {
            throw new DataPersistFailedException("Field not found for the provided fieldCode");
        }


        CropEntity isSaveCrop = cropRepository.save(mapping.convertToCropEntity(cropDTO));
        if (isSaveCrop == null) {
            throw new DataPersistFailedException("Crop not saved: Unable to persist CropEntity");
        }
    }

    @Override
    public void updateCrop(CropDTO updateCrop, String cropCode) {
        // Check if the CropEntity exists
        Optional<CropEntity> existingCropTmp = cropRepository.findById(cropCode);
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
        cropRepository.save(existingCrop);
    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> selectedCropId = cropRepository.findById(cropCode);
        if (!selectedCropId.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        } else {
            cropRepository.deleteById(cropCode);
        }
    }

    @Override
    public CropResponse getSelectCrop(String cropCode) {
        if (cropRepository.existsById(cropCode)) {
            CropEntity  cropEntityByCropCode = cropRepository.getCropEntityByCropCode(cropCode);
            return mapping.convertToCropDTO(cropEntityByCropCode);
        }else {
            return new CropErrorResponse(0,"Crop not found");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        List<CropEntity> getAllCrops = cropRepository.findAll();
        return mapping.convertToCropDTOList(getAllCrops);
    }
}