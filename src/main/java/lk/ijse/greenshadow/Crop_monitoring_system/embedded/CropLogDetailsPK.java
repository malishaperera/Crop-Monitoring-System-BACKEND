package lk.ijse.greenshadow.Crop_monitoring_system.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CropLogDetailsPK implements Serializable {
    @Column(name = "crop_code")
    private String cropCode;
    @Column(name = "log_code")
    private String logCode;

}
