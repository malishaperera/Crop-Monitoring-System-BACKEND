package lk.ijse.greenshadow.Crop_monitoring_system.entity;

import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "email")
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "staff_member_id", referencedColumnName = "staff_member_id")
    private StaffEntity staff;
}