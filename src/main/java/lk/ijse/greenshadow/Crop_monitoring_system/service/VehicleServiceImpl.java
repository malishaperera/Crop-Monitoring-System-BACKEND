package lk.ijse.greenshadow.Crop_monitoring_system.service;

import jakarta.transaction.Transactional;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.StaffRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.repository.VehicleRepository;
import lk.ijse.greenshadow.Crop_monitoring_system.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private final Mapping mapping;

    private final VehicleRepository vehicleDao;

    private final StaffRepository staffDao;


//    @Override
//    public void saveVehicle(VehicleDTO vehicleDTO) {
//
//        try{
//            List<String> vehicleCode = vehicleDao.findLastVehicleId();
//            String lastVehicleId = vehicleCode.isEmpty() ? null : vehicleCode.get(0);
//            vehicleDTO.setVehicleCode(AppUtil.generateVehicleId(lastVehicleId));
//
//            VehicleEntity isSaveVehicle = vehicleDao.save(mapping.convertToVehicleEntity(vehicleDTO));
//            if (isSaveVehicle == null) {
//                throw new DataPersistFailedException("Unable to save vehicle");
//            }
//
//        } catch (DataPersistFailedException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("Unexpected error occurred while saving vehicle", e);
//        }
//
//    }
//
//    @Override
//    public void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO) {
//        Optional<VehicleEntity> tmpStaffEntity = vehicleDao.findById(vehicleCode);
//        if(!tmpStaffEntity.isPresent()) {
//            throw new StaffNotFoundException("Staff not found");
//        }else {
//
//            StaffEntity byStaffMemberId = staffDao.findByStaffMemberId(vehicleDTO.getStaffMemberId());
//            if (byStaffMemberId == null) {
////                return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
//            }
//
//
//
//            VehicleEntity vehicleEntity = tmpStaffEntity.get();
//
//
//            vehicleEntity.setVehicleCategory(vehicleDTO.getVehicleCategory());
//            vehicleEntity.setFuelType(vehicleDTO.getFuelType());
//            vehicleEntity.setFuelType(vehicleDTO.getFuelType());
//            vehicleEntity.setStatus(vehicleDTO.getStatus());
//            vehicleEntity.setStaff(byStaffMemberId);
//            vehicleDao.save(vehicleEntity);
//
//        }
//    }
//
//    @Override
//    public void deleteVehicle(String vehicleCode) {
//        Optional<VehicleEntity> findVehicleId = vehicleDao.findById(vehicleCode);
//        if(!findVehicleId.isPresent()) {
//            throw new StaffNotFoundException("Vehicle not found");
//        }else {
//            vehicleDao.deleteById(vehicleCode);
//        }
//    }
//
//    @Override
//    public VehicleResponse getSelectVehicle(String vehicleCode) {
//        if (vehicleDao.existsById(vehicleCode)) {
//            return mapping.convertToVehicleDTO(vehicleDao.getReferenceById(vehicleCode));
//        }else {
//            return new VehicleErrorResponse(0,"Vehicle not save");
//        }
//    }
//
//    @Override
//    public List<VehicleDTO> getAllStaffs() {
//        return mapping.convertToVehicleDTOList(vehicleDao.findAll());
//    }
}