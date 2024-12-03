package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.MonitoringLogResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.MonitoringLogDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.CropLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.FieldLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.StaffLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.CropLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.StaffLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.CropNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.*;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private final MonitoringLogRepository monitoringLogDao;

    private final FieldLogDetailsRepository fieldLogDetailsRepository;
    private final CropLogDetailsRepository cropLogDetailsRepository;
    private final StaffLogDetailsRepository staffLogDetailsRepository;

    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final StaffRepository staffRepository;

    private final Mapping mapping;

    @Override
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        try {
            log.info("Starting to save Monitoring Log with observation: {}", monitoringLogDTO.getLogObservation());
            // Generate a new log code
            List<String> monitoringLogCode = monitoringLogDao.findLastMonitoringLogCode();
            String lastMonitoringLogCode = monitoringLogCode.isEmpty() ? null : monitoringLogCode.get(0);
            monitoringLogDTO.setLogCode(AppUtil.generateMonitoringLogId(lastMonitoringLogCode));
            log.info("Generated new log code: {}", monitoringLogDTO.getLogCode());

            // Convert DTO to entity and save MonitoringLogEntity
            MonitoringLogEntity logEntity = mapping.convertToMonitoringLogEntity(monitoringLogDTO);
            monitoringLogDao.save(logEntity);
            log.info("Monitoring Log saved successfully with code: {}", logEntity.getLogCode());


            // Process FieldLogDetailsEntity
            List<FieldEntity> fields = fieldRepository.findByFieldCodeIn(monitoringLogDTO.getFieldCodes());
            if (fields.size() != monitoringLogDTO.getFieldCodes().size()) {
                log.error("Some field codes are invalid or not found.");
                throw new RuntimeException("Some field codes are invalid or not found.");
            }
            List<FieldLogDetailsEntity> fieldLogDetailsEntities = fields.stream()
                    .map(field -> {
                        FieldLogDetailsPK pk = new FieldLogDetailsPK(field.getFieldCode(), monitoringLogDTO.getLogCode());
                        FieldLogDetailsEntity fieldLogDetails = new FieldLogDetailsEntity();
                        fieldLogDetails.setFieldLogDetailsPK(pk);
                        fieldLogDetails.setField(field);
                        fieldLogDetails.setLog(logEntity);
                        return fieldLogDetails;
                    })
                    .collect(Collectors.toList());
            fieldLogDetailsRepository.saveAll(fieldLogDetailsEntities);
            log.info("Field Log Details saved for log code: {}", monitoringLogDTO.getLogCode());

            // Process CropLogDetailsEntity
            List<CropEntity> crops = cropRepository.findByCropCodeIn(monitoringLogDTO.getCropCodes());
            if (crops.size() != monitoringLogDTO.getCropCodes().size()) {
                log.error("Some crop codes are invalid or not found.");
                throw new RuntimeException("Some crop codes are invalid or not found.");
            }
            List<CropLogDetailsEntity> cropLogDetailsEntities = crops.stream()
                    .map(crop -> {
                        CropLogDetailsPK pk = new CropLogDetailsPK(crop.getCropCode(), monitoringLogDTO.getLogCode());
                        CropLogDetailsEntity cropLogDetails = new CropLogDetailsEntity();
                        cropLogDetails.setCropLogDetailsPK(pk);
                        cropLogDetails.setCrop(crop);
                        cropLogDetails.setLog(logEntity);
                        return cropLogDetails;
                    })
                    .collect(Collectors.toList());
            cropLogDetailsRepository.saveAll(cropLogDetailsEntities);
            log.info("Crop Log Details saved for log code: {}", monitoringLogDTO.getLogCode());

            // Process StaffLogDetailsEntity
            List<StaffEntity> staffMembers = staffRepository.findByStaffMemberIdIn(monitoringLogDTO.getStaffMemberIds());
            if (staffMembers.size() != monitoringLogDTO.getStaffMemberIds().size()) {
                log.error("Some staff member IDs are invalid or not found.");
                throw new RuntimeException("Some staff member IDs are invalid or not found.");
            }
            List<StaffLogDetailsEntity> staffLogDetailsEntities = staffMembers.stream()
                    .map(staff -> {
                        StaffLogDetailsPK pk = new StaffLogDetailsPK(staff.getStaffMemberId(), monitoringLogDTO.getLogCode());
                        StaffLogDetailsEntity staffLogDetails = new StaffLogDetailsEntity();
                        staffLogDetails.setStaffLogDetailsPK(pk);
                        staffLogDetails.setStaff(staff);
                        staffLogDetails.setLog(logEntity);
                        return staffLogDetails;
                    })
                    .collect(Collectors.toList());
            staffLogDetailsRepository.saveAll(staffLogDetailsEntities);
            log.info("Staff Log Details saved for log code: {}", monitoringLogDTO.getLogCode());

        } catch (Exception e) {
            log.error("Error occurred while saving monitoring log", e);
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while saving monitoring log", e);
        }
    }

    @Override
    public void updateMonitoringLog(MonitoringLogDTO updateMonitoringLogDTO) {
        log.info("Updating Monitoring Log with log code: {}", updateMonitoringLogDTO.getLogCode());

        Optional<MonitoringLogEntity> tmpMonitoringLog = monitoringLogDao.findById(updateMonitoringLogDTO.getLogCode());
        if (!tmpMonitoringLog.isPresent()) {
            log.error("Monitoring Log with code {} not found", updateMonitoringLogDTO.getLogCode());
            throw new CropNotFoundException("Monitoring with code " + updateMonitoringLogDTO.getLogCode() + " not found");
        }

        MonitoringLogEntity monitoringLogEntity = tmpMonitoringLog.get();
        monitoringLogEntity.setLogObservation(updateMonitoringLogDTO.getLogObservation());
        monitoringLogEntity.setObservedImage(updateMonitoringLogDTO.getObservedImage());

        monitoringLogDao.save(monitoringLogEntity);
        log.info("Monitoring Log with code {} updated successfully", updateMonitoringLogDTO.getLogCode());
    }

    @Override
    public void deleteMonitoringLog(String logCode) {
        log.info("Deleting Monitoring Log with code: {}", logCode);

        Optional<MonitoringLogEntity> selectedMonitoringLogCode = monitoringLogDao.findById(logCode);
        if (!selectedMonitoringLogCode.isPresent()) {
            log.error("Monitoring Log with code {} not found", logCode);
            throw new CropNotFoundException("Monitoring with code " + logCode + " not found");
        }else {
            monitoringLogDao.deleteById(logCode);
            log.info("Monitoring Log with code {} deleted successfully", logCode);
        }
    }

    @Override
    public MonitoringLogResponse getSelectMonitoringLog(String logCode) {
        log.info("Fetching Monitoring Log with code: {}", logCode);

        if (monitoringLogDao.existsById(logCode)) {
            MonitoringLogEntity monitoringLogEntityByLogCode = monitoringLogDao.getMonitoringLogEntityByLogCode(logCode);
            log.info("Monitoring Log with code {} found", logCode);
            return mapping.convertToMonitoringLogDTO(monitoringLogEntityByLogCode);
        }else {
            log.error("Monitoring Log with code {} not found", logCode);
            return new MonitoringLogErrorResponse(0,"MonitoringLog not found");
        }
    }

    @Override
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        log.info("Fetching all Monitoring Logs");
        List<MonitoringLogEntity> getAllMonitoringLogs = monitoringLogDao.findAll();
        log.info("Retrieved {} Monitoring Logs", getAllMonitoringLogs.size());
        return mapping.convertToMonitoringLogDTOList(getAllMonitoringLogs);
    }
}