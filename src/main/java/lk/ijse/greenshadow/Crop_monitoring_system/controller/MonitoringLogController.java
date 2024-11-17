package lk.ijse.greenshadow.Crop_monitoring_system.controller;



import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.MonitoringLogDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.FieldNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.service.MonitoringLogService;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MonitoringLogController {

    @Autowired
    private final MonitoringLogService monitoringLogService;

    @GetMapping("/health")
    public String healthCheck(){
        return "MonitoringLog is running";
    }

    /**To Do CRUD Operation**/
    //MonitoringLog save
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveMonitoringLog(
            @RequestParam("logObservation") String logObservation,
            @RequestParam("observedImage") MultipartFile observedImage) {
        try {
            byte[] imageBytes = observedImage.getBytes();
            String base64Image = AppUtil.toBase64ProfilePic(imageBytes);

            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();
            monitoringLogDTO.setLogDate(new Date()); // Current date
            monitoringLogDTO.setLogObservation(logObservation);
            monitoringLogDTO.setObservedImage(base64Image);

            monitoringLogService.saveMonitoringLog(monitoringLogDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            return new ResponseEntity<>("Data persist failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Update Monitoring
    @PatchMapping(value = "/{logCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMonitoringLog(
            @PathVariable("logCode") String logCode,
            @RequestParam("logObservation") String logObservation,
            @RequestParam("observedImage") MultipartFile observedImage) {


        try {
            byte[] imageBytes = observedImage.getBytes();
            String base64Image = AppUtil.toBase64ProfilePic(imageBytes);

            MonitoringLogDTO updateMonitoringLogDTO = new MonitoringLogDTO();
//            monitoringLogDTO.setLogDate(new Date());
            updateMonitoringLogDTO.setLogCode(logCode);
            updateMonitoringLogDTO.setLogObservation(logObservation);
            updateMonitoringLogDTO.setObservedImage(base64Image);

            monitoringLogService.updateMonitoringLog(updateMonitoringLogDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataPersistFailedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //MonitoringLog update
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMonitoringLog(@PathVariable("id") String logCode) {
        try {
            monitoringLogService.deleteMonitoringLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //MonitoringLog Get
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogResponse getSelectedMonitoringLog(@PathVariable("id") String logCode){
        return monitoringLogService.getSelectMonitoringLog(logCode);
    }

    //Get All MonitoringLog
    @GetMapping(value = "allMonitoringLogs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        return monitoringLogService.getAllMonitoringLogs();
    }
}