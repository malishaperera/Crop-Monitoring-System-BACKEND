package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffErrorResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.StaffResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dao.StaffDao;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.StaffDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.DataPersistFailedException;
import lk.ijse.greenshadow.Crop_monitoring_system.exception.StaffNotFoundException;
import lk.ijse.greenshadow.Crop_monitoring_system.util.AppUtil;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private Mapping mapping;


    @Override
    public void saveStaff(StaffDTO staffDTO) {
        List<String> staffIds = staffDao.findLastStaffMemberId();
        String lastStaffId = staffIds.isEmpty() ? null : staffIds.get(0);
        staffDTO.setStaffMemberId(AppUtil.generateNextStaffId(lastStaffId));

        var staffEntity = mapping.convertToStaffEntity(staffDTO);
        var isSaveStaff = staffDao.save(staffEntity);
        if (isSaveStaff == null) {
            throw new DataPersistFailedException("Can't save staff");
        }
    }

    @Override
    public void updateStaff(String staffMemberId, StaffDTO staff) {
        Optional<StaffEntity> tmpStaffEntity = staffDao.findById(staffMemberId);
        if(!tmpStaffEntity.isPresent()) {
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
        }
    }

    @Override
    public void deleteStaff(String staffMemberId) {
        Optional<StaffEntity> findMemberId = staffDao.findById(staffMemberId);
        if(!findMemberId.isPresent()) {
            throw new StaffNotFoundException("Staff not found");
        }else {
            staffDao.deleteById(staffMemberId);
        }

    }

    @Override
    public StaffResponse getSelectStaff(String staffMemberId) {
        if (staffDao.existsById(staffMemberId)) {
            return mapping.convertToStaffDTO(staffDao.getReferenceById(staffMemberId));
        }else {
            return new StaffErrorResponse(0,"Staff not save");
        }
    }

    @Override
    public List<StaffDTO> getAllStaffs() {
        return mapping.convertToStaffDTOList(staffDao.findAll());
    }
}