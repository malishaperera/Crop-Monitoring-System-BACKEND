package lk.ijse.greenshadow.Crop_monitoring_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.FieldStaffDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.association.StaffLogDetailsEntity;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Gender;
import lk.ijse.greenshadow.Crop_monitoring_system.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "staff")
public class StaffEntity {
    @Id
    @Column(name = "staff_member_id")
    private String staffMemberId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "designation")
    private String designation;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "joined_date")
    private Date joinedDate;

    @Column(name = "date_of_birth")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date DOB;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    @Column(name = "address_line_4")
    private String addressLine4;

    @Column(name = "address_line_5")
    private String addressLine5;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EquipmentEntity equipment;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY) // Cascade if needed
    @JsonIgnore
    private List<VehicleEntity> vehicleList = new ArrayList<>();

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserEntity user;

    //Associate

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FieldStaffDetailsEntity> fieldStaffDetailsList = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StaffLogDetailsEntity> staffLogDetailsList = new ArrayList<>();

}