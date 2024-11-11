package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.CropResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.CropDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.FieldDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.CropDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.CropService;
import lk.ijse.greenshadow.Crop_monitoring_system.service.EquipmentService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.yaml.snakeyaml.nodes.NodeId.mapping;

@RestController
@RequestMapping("/api/v1/crops")
@RequiredArgsConstructor
public class CropController {
    @Autowired
    private final CropService cropService;

    private final FieldDao fieldDao; // Inject FieldDao
    @Autowired
    private CropDao cropDao;


    @GetMapping("/health")
    public String healthCheck(){
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
            byte[] imageByteCollection1 = cropImage.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            // Fetch the FieldEntity using the fieldCode
            FieldEntity field = fieldDao.findByFieldCode(fieldCode);
            if (field == null) {
                return new ResponseEntity<>("Field not found", HttpStatus.NOT_FOUND);
            }

            CropDTO buildCropDTO = new CropDTO();
            buildCropDTO.setCropCommonName(cropCommonName);
            buildCropDTO.setCropScientificName(cropScientificName);
            buildCropDTO.setCropImage(base64ProfilePic1);
            buildCropDTO.setCategory(category);
            buildCropDTO.setCropSeason(cropSeason);
            buildCropDTO.setField(field);

            cropService.saveCrop(buildCropDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistFailedException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Crop
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
            byte[] imageByteCollection1 = cropImage.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            // Fetch the FieldEntity using the fieldCode
            FieldEntity field = fieldDao.findByFieldCode(fieldCode);
            if (field == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            CropDTO updateCrop = new CropDTO();
            updateCrop.setCropCode(cropCode);
            updateCrop.setCropCommonName(cropCommonName);
            updateCrop.setCropScientificName(cropScientificName);
            updateCrop.setCropImage(base64ProfilePic1);
            updateCrop.setCategory(category);
            updateCrop.setCropSeason(cropSeason);
            updateCrop.setField(field);

            cropService.updateCrop(updateCrop);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Delete Crop
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCrop(@PathVariable("id") String cropCode) {
        try {
            cropService.deleteCrop(cropCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (CropNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Crop
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CropResponse getSelectedCrop(@PathVariable("id") String cropCode){
        return cropService.getSelectCrop(cropCode);
    }

    //Get All Crop
    @GetMapping(value = "allCrops", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllCrops() {
        return cropService.getAllCrops();
    }
}