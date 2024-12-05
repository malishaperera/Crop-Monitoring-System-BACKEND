package lk.ijse.greenshadow.Crop_monitoring_system.dto.impl;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ijse.greenshadow.Crop_monitoring_system.dto.SuperDTO;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.EquipmentTypes;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements SuperDTO {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    private String staffMemberId;
    @NotNull(message = "Role cannot be null")
    private Role role;
}
