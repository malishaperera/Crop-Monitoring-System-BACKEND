package lk.ijse.greenshadow.Crop_monitoring_system.controller;


import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.CropService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crops")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5502", "http://localhost:5502"}) // Allow specific origins
public class CropController {
    private final CropService cropService;

    @GetMapping("/health")
    public String healthCheck(){
        log.info("Health check for Crop service.");
        return "Crop is running";
    }

    /**To Do CRUD Operation**/

    //Save Crop
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveCrop(
            @RequestParam("cropCommonName") String cropCommonName,
            @RequestParam("cropScientificName") String cropScientificName,
            @RequestParam("cropImage") MultipartFile cropImage,
            @RequestParam("category") String category,
            @RequestParam("cropSeason") String cropSeason,
            @RequestParam("fieldCode") String fieldCode){

        try {
            log.info("Saving crop: {} (Common Name: {}, Scientific Name: {})", cropCommonName, cropCommonName, cropScientificName);
            byte[] imageByteCollection1 = cropImage.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            CropDTO buildCropDTO = new CropDTO();
            buildCropDTO.setCropCommonName(cropCommonName);
            buildCropDTO.setCropScientificName(cropScientificName);
            buildCropDTO.setCropImage(base64ProfilePic1);
            buildCropDTO.setCategory(category);
            buildCropDTO.setCropSeason(cropSeason);
            buildCropDTO.setFieldCode(fieldCode);

            cropService.saveCrop(buildCropDTO);
            log.info("Crop saved successfully: {}", cropCommonName);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistFailedException e){
            log.error("Error saving crop: {}", e.getMessage());
            return new ResponseEntity<>("Crop not saved: Field not found for the provided fieldCode", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Unexpected error while saving crop: {}", e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Crop
    @PatchMapping(value = "/{cropCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCrop(
            @PathVariable("cropCode") String cropCode,
            @RequestParam("cropCommonName") String cropCommonName,
            @RequestParam("cropScientificName") String cropScientificName,
            @RequestParam("cropImage") MultipartFile cropImage,
            @RequestParam("category") String category,
            @RequestParam("cropSeason") String cropSeason,
            @RequestParam("fieldCode") String fieldCode) {

        try {
            log.info("Updating crop: {} (Common Name: {}, Scientific Name: {})", cropCode, cropCommonName, cropScientificName);
            byte[] imageByteCollection1 = cropImage.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            CropDTO updateCrop = new CropDTO();
            updateCrop.setCropCommonName(cropCommonName);
            updateCrop.setCropScientificName(cropScientificName);
            updateCrop.setCropImage(base64ProfilePic1);
            updateCrop.setCategory(category);
            updateCrop.setCropSeason(cropSeason);
            updateCrop.setFieldCode(fieldCode);

            cropService.updateCrop(updateCrop, cropCode);
            log.info("Crop updated successfully: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            log.error("Error updating crop: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while updating crop: {}", e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCrop(@PathVariable("id") String cropCode) {
        try {
            log.info("Deleting crop with code: {}", cropCode);
            cropService.deleteCrop(cropCode);
            log.info("Crop deleted successfully: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (CropNotFoundException e){
            log.error("Crop not found for deletion: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Unexpected error while deleting crop: {}", cropCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Crop
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CropResponse getSelectedCrop(@PathVariable("id") String cropCode){
        log.info("Fetching crop with code: {}", cropCode);
        return cropService.getSelectCrop(cropCode);
    }

    //Get All Crop
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "allCrops", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllCrops() {
        log.info("Fetching all crops");
        return cropService.getAllCrops();
    }


    //custom getAll crop codes
//    @GetMapping(value = "/crops/allCropsCodes", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<String>> getAllCropsCode() {
//        List<String> cropCodes = cropService.getAllCropsCods();
//        return ResponseEntity.ok(cropCodes);
//    }
}