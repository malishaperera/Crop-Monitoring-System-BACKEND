package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;


import jakarta.validation.constraints.NotNull;
import lk.ijse.greenshadow.Crop_monitoring_system.customObj.VehicleResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleDTO implements SuperDTO, VehicleResponse {
    private String vehicleCode;
    @NotNull(message = "License plate number cannot be null")
    private String licensePlateNumber;
    @NotNull(message = "Vehicle category cannot be null")
    private String vehicleCategory;
    @NotNull(message = "Fuel type cannot be null")
    private String fuelType;
    @NotNull(message = "Status cannot be null")
    private Status status;
    private String remarks;
    private String staffMemberId;
}