package lk.ijse.greenshadow.Crop_monitoring_system.entity.association;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.FieldLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field_log_details")
public class FieldLogDetailsEntity {
    @EmbeddedId
    private FieldLogDetailsPK fieldLogDetailsPK;

    @ManyToOne
    @MapsId("fieldCode")
    @JoinColumn(name = "field_code")
    private FieldEntity field;

    @ManyToOne
    @MapsId("logCode")  // Ensure that this is the correct field name in your composite key
    @JoinColumn(name = "log_code")
    private MonitoringLogEntity log;
}
