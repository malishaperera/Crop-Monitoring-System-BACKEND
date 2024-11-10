package lk.ijse.greenshadow.Crop_monitoring_system.entity.association;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.StaffLogDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.MonitoringLogEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff_log_details")
public class StaffLogDetailsEntity {
    @EmbeddedId
    private StaffLogDetailsPK staffLogDetailsPK;

    @ManyToOne
    @MapsId("staffMemberId")
    @JoinColumn(name = "staff_member_id")
    private StaffEntity staff;

    @ManyToOne
    @MapsId("logCode")
    @JoinColumn(name = "log_code")
    private MonitoringLogEntity log;
}
