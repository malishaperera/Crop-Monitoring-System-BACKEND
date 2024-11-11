package lk.ijse.greenshadow.Crop_monitoring_system.entity;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.EquipmentTypes;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "equipments")
@Entity
public class EquipmentEntity {
    @Id
    @Column(name = "equipment_id")
    private String equipmentId;
    @Column(name = "equipment_name")
    private String name;
    @Column(name = "equipment_type")
    private EquipmentTypes equipmentType;
    @Column(name = "availability_status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_code", referencedColumnName = "field_code")
    private FieldEntity field;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "staff_member_id", referencedColumnName = "staff_member_id")
    private StaffEntity staff;
}