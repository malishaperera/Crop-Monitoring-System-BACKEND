package lk.ijse.greenshadow.Crop_monitoring_system.customObj;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorResponse implements FieldResponse,Serializable {
    private int errorCode;
    public String errorMessage;
}
