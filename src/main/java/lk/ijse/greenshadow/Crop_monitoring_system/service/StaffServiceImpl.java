package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.StaffNotFoundException;
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
public class StaffServiceImpl implements StaffService{

    @Autowired
    private StaffRepository staffDao;

    @Autowired
    private Mapping mapping;

    @Override
    public void saveStaff(StaffDTO staffDTO) {
        log.info("Starting to save staff with data: {}", staffDTO);
        List<String> staffIds = staffDao.findLastStaffMemberId();
        String lastStaffId = staffIds.isEmpty() ? null : staffIds.get(0);
        staffDTO.setStaffMemberId(AppUtil.generateNextStaffId(lastStaffId));

        var staffEntity = mapping.convertToStaffEntity(staffDTO);
        var isSaveStaff = staffDao.save(staffEntity);
        if (isSaveStaff == null) {
            log.error("Failed to save staff: {}", staffDTO);
            throw new DataPersistFailedException("Can't save staff");
        }else {
            log.info("Staff saved successfully with ID: {}", staffDTO.getStaffMemberId());
        }
    }

    @Override
    public void updateStaff(String staffMemberId, StaffDTO staff) {
        log.info("Starting to update staff with ID: {} and data: {}", staffMemberId, staff);
        Optional<StaffEntity> tmpStaffEntity = staffDao.findById(staffMemberId);

        if(!tmpStaffEntity.isPresent()) {
            log.error("Staff with ID: {} not found for update", staffMemberId);
            throw new StaffNotFoundException("Staff not found");
        }else {
            StaffEntity staffEntity = tmpStaffEntity.get();

            staffEntity.setFirstName(staff.getFirstName());
            staffEntity.setLastName(staff.getLastName());
            staffEntity.setDesignation(staff.getDesignation());
            staffEntity.setGender(staff.getGender());
            staffEntity.setJoinedDate(staff.getJoinedDate());
            staffEntity.setDOB(staff.getDOB());
            staffEntity.setAddressLine1(staff.getAddressLine1());
            staffEntity.setAddressLine2(staff.getAddressLine2());
            staffEntity.setAddressLine3(staff.getAddressLine3());
            staffEntity.setAddressLine4(staff.getAddressLine4());
            staffEntity.setAddressLine5(staff.getAddressLine5());
            staffEntity.setContactNo(staff.getContactNo());
            staffEntity.setEmail(staff.getEmail());
            staffEntity.setRole(staff.getRole());

            staffDao.save(staffEntity);
            log.info("Staff with ID: {} updated successfully", staffMemberId);
        }
    }

    @Override
    public void deleteStaff(String staffMemberId) {
        log.info("Starting to delete staff with ID: {}", staffMemberId);
        Optional<StaffEntity> findMemberId = staffDao.findById(staffMemberId);
        if(!findMemberId.isPresent()) {
            log.error("Staff with ID: {} not found for deletion", staffMemberId);
            throw new StaffNotFoundException("Staff not found");
        }else {
            staffDao.deleteById(staffMemberId);
            log.info("Staff with ID: {} deleted successfully", staffMemberId);
        }
    }

    @Override
    public StaffResponse getSelectStaff(String staffMemberId) {
        log.info("Fetching staff details for ID: {}", staffMemberId);
        if (staffDao.existsById(staffMemberId)) {
            log.info("Staff with ID: {} found", staffMemberId);
            return mapping.convertToStaffDTO(staffDao.getReferenceById(staffMemberId));
        }else {
            log.error("Staff with ID: {} not found", staffMemberId);
            return new StaffErrorResponse(0,"Staff not save");
        }
    }

    @Override
    public List<StaffDTO> getAllStaffs() {
        log.info("Fetching all staff members");
        return mapping.convertToStaffDTOList(staffDao.findAll());
    }

    //custom
//    @Override
//    public StaffDTO existByStaffMember(String staffMemberId) {
//        StaffEntity byStaffMemberId = staffDao.findByStaffMemberId(staffMemberId);
//        return mapping.convertToStaffDTO(byStaffMemberId);
//    }
}