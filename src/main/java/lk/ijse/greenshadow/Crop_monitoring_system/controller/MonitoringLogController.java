package lk.ijse.greenshadow.Crop_monitoring_system.controller;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.MonitoringLogDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.MonitoringLogService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/monitoringlogs")
@RequiredArgsConstructor
@Slf4j
public class MonitoringLogController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringLogController.class);
    private final MonitoringLogService monitoringLogService;

    @GetMapping("/health")
    public String healthCheck(){
        return "MonitoringLog is running";
    }

    /**To Do CRUD Operation**/
    //MonitoringLog save
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveMonitoringLog(
            @RequestParam("logObservation") String logObservation,
            @RequestParam(value = "observedImage", required = false) MultipartFile observedImage,
            @RequestParam("fieldCodes") List<String> fieldCodes,
            @RequestParam("staffMemberIds") List<String> staffMemberIds,
            @RequestParam("cropCodes") List<String> cropCodes) {

        try {
            logger.info("Received request to save monitoring log with observation: {}", logObservation);

            // Validate fieldCodes and cropCodes
            if (fieldCodes.isEmpty()) {
                log.warn("Field codes are missing.");
                return ResponseEntity.badRequest().body("Field codes must be provided.");
            }

            if (cropCodes.isEmpty()) {
                log.warn("Crop codes are missing.");
                return ResponseEntity.badRequest().body("Crop codes must be provided.");
            }

            if (staffMemberIds.isEmpty()) {
                log.warn("Staff member IDs are missing.");
                return ResponseEntity.badRequest().body("Staff member IDs must be provided.");
            }

            String base64Image = null;
            if (observedImage != null && !observedImage.isEmpty()) {
                byte[] imageBytes = observedImage.getBytes();
                base64Image = AppUtil.toBase64ProfilePic(imageBytes);
                log.info("Image received and converted to Base64.");
            }

            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();
            monitoringLogDTO.setLogDate(new Date());
            monitoringLogDTO.setLogObservation(logObservation);
            monitoringLogDTO.setObservedImage(base64Image);
            monitoringLogDTO.setFieldCodes(fieldCodes);
            monitoringLogDTO.setStaffMemberIds(staffMemberIds);
            monitoringLogDTO.setCropCodes(cropCodes);

            monitoringLogService.saveMonitoringLog(monitoringLogDTO);
            log.info("Monitoring log saved successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body("Monitoring log saved successfully.");
        } catch (DataPersistFailedException e) {
            log.error("Data persist failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data persist failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    //Update Monitoring
    @PatchMapping(value = "/{logCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMonitoringLog(
            @PathVariable("logCode") String logCode,
            @RequestParam("logObservation") String logObservation,
            @RequestParam(value = "observedImage", required = false) MultipartFile observedImage,
            @RequestParam(value = "fieldCodes", required = false) List<String> fieldCodes,
            @RequestParam(value = "staffMemberIds", required = false) List<String> staffMemberIds,
            @RequestParam(value = "cropCodes", required = false) List<String> cropCodes) {

        try {
            log.info("Updating monitoring log with logCode: {}", logCode);

            String base64Image = null;
            if (observedImage != null && !observedImage.isEmpty()) {
                byte[] imageBytes = observedImage.getBytes();
                String base64Images    = base64Image = AppUtil.toBase64ProfilePic(imageBytes);
                log.info("Image received and converted to Base64.");
            }else {
                System.out.println("No image.");
            }

            MonitoringLogDTO updateMonitoringLogDTO = new MonitoringLogDTO();
            updateMonitoringLogDTO.setLogCode(logCode);
            updateMonitoringLogDTO.setLogObservation(logObservation);
            updateMonitoringLogDTO.setObservedImage(base64Image);

            if (fieldCodes != null && !fieldCodes.isEmpty()) {
                updateMonitoringLogDTO.setFieldCodes(fieldCodes);
            }

            if (staffMemberIds != null && !staffMemberIds.isEmpty()) {
                updateMonitoringLogDTO.setStaffMemberIds(staffMemberIds);
            }

            if (cropCodes != null && !cropCodes.isEmpty()) {
                updateMonitoringLogDTO.setCropCodes(cropCodes);
            }

            monitoringLogService.updateMonitoringLog(updateMonitoringLogDTO);
            log.info("Monitoring log updated successfully.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            log.error("Monitoring log update failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error during monitoring log update: {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //MonitoringLog update
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMonitoringLog(@PathVariable("id") String logCode) {
        try {
            log.info("Deleting monitoring log with logCode: {}", logCode);
            monitoringLogService.deleteMonitoringLog(logCode);
            log.info("Monitoring log deleted successfully.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            log.error("Field not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Error during monitoring log deletion: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //MonitoringLog Get
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogResponse getSelectedMonitoringLog(@PathVariable("id") String logCode){
        log.info("Fetching monitoring log with logCode: {}", logCode);
        return monitoringLogService.getSelectMonitoringLog(logCode);
    }

    //Get All MonitoringLog
    @GetMapping(value = "allMonitoringLogs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        log.info("Fetching all monitoring logs.");
        return monitoringLogService.getAllMonitoringLogs();
    }
}