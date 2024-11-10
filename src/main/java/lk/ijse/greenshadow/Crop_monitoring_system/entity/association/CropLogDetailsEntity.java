package lk.ijse.greenshadow.Crop_monitoring_system.entity.association;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.CropLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.CropEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crop_log_details")
public class CropLogDetailsEntity {
    @EmbeddedId
    private CropLogDetailsPK cropLogDetailsPK;

    @ManyToOne
    @MapsId("cropCode")
    @JoinColumn(name = "crop_code")
    private CropEntity crop;

    @ManyToOne
    @MapsId("logCode")
    @JoinColumn(name = "log_code")
    private MonitoringLogEntity log;
}
