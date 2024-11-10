package lk.ijse.greenshadow.Crop_monitoring_system.entity.association;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.embedded.FieldStaffDetailsPK;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.FieldEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.StaffEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field_staff_details")
public class FieldStaffDetailsEntity {
    @EmbeddedId
    private FieldStaffDetailsPK fieldStaffDetailsPK;

    @ManyToOne
    @MapsId("fieldCode")
    @JoinColumn(name = "field_code")
    private FieldEntity field;

    @ManyToOne
    @MapsId("staffMemberId")
    @JoinColumn(name = "staff_member_id")
    private StaffEntity staff;
}
