package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.FieldResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.FieldDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.FieldService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:5502", "http://localhost:5502"}) // Allow specific origins
@Slf4j
public class FieldController {

    private static final Logger logger = LoggerFactory.getLogger(FieldController.class);

    @Autowired
    private final FieldService fieldService;

    @GetMapping("/health")
    public String healthCheck(){
        logger.info("Health check endpoint called");
        return "Field is running";
    }

    /**To Do CRUD Operation**/
    //Save Field
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveField(
            @RequestParam("fieldName") String fieldName,
            @RequestParam("fieldLocation") String  fieldLocation,
            @RequestParam("fieldSize") double fieldSize,
            @RequestParam(value = "fieldImage1",required = false) MultipartFile fieldImage1,
            @RequestParam(value = "fieldImage2",required = false) MultipartFile fieldImage2) {
        try {
            logger.info("Received request to save field: {}", fieldName);

            String[] coords = fieldLocation.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            Point fieldLocationP = new Point(x, y);

            byte[] imageByteCollection1 = fieldImage1.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            byte[] imageByteCollection2 = fieldImage2.getBytes();
            String base64ProfilePic2 = AppUtil.toBase64ProfilePic(imageByteCollection2);

            FieldDTO buildFieldDTO = new FieldDTO();
            buildFieldDTO.setFieldName(fieldName);
            buildFieldDTO.setFieldLocation(fieldLocationP);
            buildFieldDTO.setFieldSize(fieldSize);
            buildFieldDTO.setFieldImage1(base64ProfilePic1);
            buildFieldDTO.setFieldImage2(base64ProfilePic2);

            fieldService.saveField(buildFieldDTO);

            logger.info("Field saved successfully: {}", fieldName);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistFailedException e){
            logger.error("Error saving field: {}", fieldName, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error occurred while saving field: {}", fieldName, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Field
    @PatchMapping(value = "/{fieldCode}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateField(
            @PathVariable("fieldCode") String fieldCode,
            @RequestParam("fieldName") String fieldName,
            @RequestParam("fieldLocation") String  fieldLocation,
            @RequestParam("fieldSize") double fieldSize,
            @RequestParam("fieldImage1") MultipartFile fieldImage1,
            @RequestParam("fieldImage2") MultipartFile fieldImage2){

        logger.info("Received request to update field with code: {}", fieldCode);
        try {

            String[] coords = fieldLocation.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            Point fieldLocationP = new Point(x, y);

            byte[] imageByteCollection1 = fieldImage1.getBytes();
            String base64ProfilePic1 = AppUtil.toBase64ProfilePic(imageByteCollection1);

            byte[] imageByteCollection2 = fieldImage2.getBytes();
            String base64ProfilePic2 = AppUtil.toBase64ProfilePic(imageByteCollection2);

            FieldDTO updateField = new FieldDTO();
            updateField.setFieldCode(fieldCode);
            updateField.setFieldName(fieldName);
            updateField.setFieldLocation(fieldLocationP);
            updateField.setFieldSize(fieldSize);
            updateField.setFieldImage1(base64ProfilePic1);
            updateField.setFieldImage2(base64ProfilePic2);

            fieldService.updateField(updateField);
            logger.info("Field updated successfully: {}", fieldCode);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (FieldNotFoundException e){
            logger.warn("Field not found for update: {}", fieldCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("An error occurred while updating the field: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Field
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteField(@PathVariable("id") String fieldCode) {
        logger.info("Received request to delete field with code: {}", fieldCode);
        try {
            fieldService.deleteField(fieldCode);
            logger.info("Field deleted successfully: {}", fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            logger.warn("Field not found for deletion: {}", fieldCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("An error occurred while deleting the field: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Field
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldResponse getSelectedField(@PathVariable("id") String fieldCode){
        logger.info("Received request to fetch field details for code: {}", fieldCode);
        return fieldService.getSelectField(fieldCode);
    }

    @PreAuthorize("hasRole('ADMIN')")
    //Get All Field
    @GetMapping(value = "allFields", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getAllFields() {
        logger.info("Received request to fetch all fields");
        return fieldService.getAllFields();
    }

//
//    private String convertImageToBase64(String imagePath) throws IOException {
//        // Read image file
//        Path path = Paths.get(imagePath);
//        byte[] imageBytes = Files.readAllBytes(path);
//
//        // Convert the byte array to a Base64 string
//        return Base64.getEncoder().encodeToString(imageBytes);
//    }
}