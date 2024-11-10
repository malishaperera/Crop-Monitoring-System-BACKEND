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
public class FieldStaffDetailsPK implements Serializable {
    @Column(name = "field_code")
    private String fieldCode;
    @Column(name = "staff_member_id")
    private String staffMemberId;

}
