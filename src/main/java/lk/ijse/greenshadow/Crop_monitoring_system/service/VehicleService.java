package lk.ijse.greenshadow.Crop_monitoring_system.service;

import lk.ijse.greenshadow.Crop_monitoring_system.customObj.VehicleResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.impl.VehicleDTO;

import java.util.List;

public interface VehicleService {
    void saveVehicle(VehicleDTO vehicleDTO);

    void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO);

    void deleteVehicle(String vehicleCode);

    VehicleResponse getSelectVehicle(String vehicleCode);

    List<VehicleDTO> getAllStaffs();
}
