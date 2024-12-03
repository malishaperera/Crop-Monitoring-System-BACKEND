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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private final MonitoringLogRepository monitoringLogDao;

    private final FieldLogDetailsRepository fieldLogDetailsRepository;
    private final CropLogDetailsRepository cropLogDetailsRepository;
    private final StaffLogDetailsRepository staffLogDetailsRepository;

    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final StaffRepository staffRepository;

    private final Mapping mapping;
//
//    @Override
//    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
//        try {
//            List<String> monitoringLogCode = monitoringLogDao.findLastMonitoringLogCode();
//            String lastMonitoringLogCode = monitoringLogCode.isEmpty() ? null : monitoringLogCode.get(0);
//            monitoringLogDTO.setLogCode(AppUtil.generateMonitoringLogId(lastMonitoringLogCode));
//
//            MonitoringLogEntity logEntity = mapping.convertToMonitoringLogEntity(monitoringLogDTO);
//            monitoringLogDao.save(logEntity);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Unexpected error occurred while saving monitoring log", e);
//        }
//    }
    @Override
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        try {
            // Generate a new log code
            List<String> monitoringLogCode = monitoringLogDao.findLastMonitoringLogCode();
            String lastMonitoringLogCode = monitoringLogCode.isEmpty() ? null : monitoringLogCode.get(0);
            monitoringLogDTO.setLogCode(AppUtil.generateMonitoringLogId(lastMonitoringLogCode));

            // Convert DTO to entity and save MonitoringLogEntity
            MonitoringLogEntity logEntity = mapping.convertToMonitoringLogEntity(monitoringLogDTO);
            monitoringLogDao.save(logEntity);

            // Process FieldLogDetailsEntity
            List<FieldEntity> fields = fieldRepository.findByFieldCodeIn(monitoringLogDTO.getFieldCodes());
            if (fields.size() != monitoringLogDTO.getFieldCodes().size()) {
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

            // Process CropLogDetailsEntity
            List<CropEntity> crops = cropRepository.findByCropCodeIn(monitoringLogDTO.getCropCodes());
            if (crops.size() != monitoringLogDTO.getCropCodes().size()) {
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

            // Process StaffLogDetailsEntity
            List<StaffEntity> staffMembers = staffRepository.findByStaffMemberIdIn(monitoringLogDTO.getStaffMemberIds());
            if (staffMembers.size() != monitoringLogDTO.getStaffMemberIds().size()) {
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while saving monitoring log", e);
        }
    }

    @Override
    public void updateMonitoringLog(MonitoringLogDTO updateMonitoringLogDTO) {
        Optional<MonitoringLogEntity> tmpMonitoringLog = monitoringLogDao.findById(updateMonitoringLogDTO.getLogCode());
        if (!tmpMonitoringLog.isPresent()) {
            throw new CropNotFoundException("Monitoring with code " + updateMonitoringLogDTO.getLogCode() + " not found");
        }

        MonitoringLogEntity monitoringLogEntity = tmpMonitoringLog.get();
        monitoringLogEntity.setLogObservation(updateMonitoringLogDTO.getLogObservation());
        monitoringLogEntity.setObservedImage(updateMonitoringLogDTO.getObservedImage());

        monitoringLogDao.save(monitoringLogEntity);
    }

    @Override
    public void deleteMonitoringLog(String logCode) {
        Optional<MonitoringLogEntity> selectedMonitoringLogCode = monitoringLogDao.findById(logCode);
        if (!selectedMonitoringLogCode.isPresent()) {
            throw new CropNotFoundException("Monitoring with code " + logCode + " not found");
        }else {
            monitoringLogDao.deleteById(logCode);
        }
    }

    @Override
    public MonitoringLogResponse getSelectMonitoringLog(String logCode) {
        if (monitoringLogDao.existsById(logCode)) {
            MonitoringLogEntity monitoringLogEntityByLogCode = monitoringLogDao.getMonitoringLogEntityByLogCode(logCode);
            return mapping.convertToMonitoringLogDTO(monitoringLogEntityByLogCode);
        }else {
            return new MonitoringLogErrorResponse(0,"MonitoringLog not found");
        }
    }

    @Override
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        List<MonitoringLogEntity> getAllMonitoringLogs = monitoringLogDao.findAll();
        return mapping.convertToMonitoringLogDTOList(getAllMonitoringLogs);
    }
}