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
    @Column(name = "equipment_id", nullable = false, updatable = false)
    private String equipmentId;
    @Column(name = "equipment_name")
    private String name;
    @Column(name = "equipment_type")
    @Enumerated(EnumType.STRING)
    private EquipmentTypes equipmentType;
    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "field_code")
    private FieldEntity field;

    @OneToOne
    @JoinColumn(name = "staff_member_id")
    private StaffEntity staff;
}