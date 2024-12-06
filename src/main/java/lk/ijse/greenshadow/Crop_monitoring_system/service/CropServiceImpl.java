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
        log.info("Saving crop: {}", cropDTO.getCropCommonName());
        List<String> cropCode = cropRepository.findLastCropCode();
        String lastCropCode = cropCode.isEmpty() ? null : cropCode.get(0);
        cropDTO.setCropCode(AppUtil.generateNextCropId(lastCropCode));

        if (cropRepository.existsById(cropDTO.getCropCode())) {
            log.error("Crop with code {} already exists", cropDTO.getCropCode());
            throw new DataPersistFailedException("Crop not saved: Crop with the same code already exists");
        }

        FieldEntity field = fieldDao.findByFieldCode(cropDTO.getFieldCode());
        if (field == null) {
            log.error("Field not found for fieldCode: {}", cropDTO.getFieldCode());
            throw new DataPersistFailedException("Field not found for the provided fieldCode");
        }

        CropEntity isSaveCrop = cropRepository.save(mapping.convertToCropEntity(cropDTO));
        if (isSaveCrop == null) {
            log.error("Unable to persist CropEntity for crop code: {}", cropDTO.getCropCode());
            throw new DataPersistFailedException("Crop not saved: Unable to persist CropEntity");
        }
        log.info("Crop saved successfully: {}", cropDTO.getCropCode());
    }

    @Override
    public void updateCrop(CropDTO updateCrop, String cropCode) {
        log.info("Updating crop: {}", cropCode);
        // Check if the CropEntity exists
        Optional<CropEntity> existingCropTmp = cropRepository.findById(cropCode);
        if (existingCropTmp.isEmpty()) {
            log.error("Crop not found for crop code: {}", cropCode);
            throw new CropNotFoundException("Crop with code " + cropCode + " not found");
        }

        // Validate and fetch FieldEntity using fieldCode
        FieldEntity field = fieldDao.findByFieldCode(updateCrop.getFieldCode());
        if (field == null) {
            log.error("Field not found for fieldCode: {}", updateCrop.getFieldCode());
            throw new DataPersistFailedException("Field not found for the provided fieldCode");
        }

        CropEntity existingCrop = existingCropTmp.get();

        // Update the CropEntity with new data
        existingCrop.setCropCommonName(updateCrop.getCropCommonName());
        existingCrop.setCropScientificName(updateCrop.getCropScientificName());
        existingCrop.setCropImage(updateCrop.getCropImage());
        existingCrop.setCategory(updateCrop.getCategory());
        existingCrop.setCropSeason(updateCrop.getCropSeason());
        existingCrop.setField(field);

        // Save the updated entity
        cropRepository.save(existingCrop);
        log.info("Crop updated successfully: {}", cropCode);
    }

    @Override
    public void deleteCrop(String cropCode) {
        log.info("Deleting crop with code: {}", cropCode);
        Optional<CropEntity> selectedCropId = cropRepository.findById(cropCode);
        if (!selectedCropId.isPresent()) {
            log.error("Crop not found for deletion: {}", cropCode);
            throw new CropNotFoundException("Crop not found");
        } else {
            cropRepository.deleteById(cropCode);
            log.info("Crop deleted successfully: {}", cropCode);
        }
    }

    @Override
    public CropResponse getSelectCrop(String cropCode) {
        log.info("Fetching crop with code: {}", cropCode);
        if (cropRepository.existsById(cropCode)) {
            CropEntity  cropEntityByCropCode = cropRepository.getCropEntityByCropCode(cropCode);
            log.info("Fetched crop: {}", cropCode);
            return mapping.convertToCropDTO(cropEntityByCropCode);
        }else {
            log.warn("Crop not found for crop code: {}", cropCode);
            return new CropErrorResponse(0,"Crop not found");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        log.info("Fetching all crops");
        List<CropEntity> getAllCrops = cropRepository.findAll();
        log.info("Retrieved {} crops", getAllCrops.size());
        return mapping.convertToCropDTOList(getAllCrops);
    }
}