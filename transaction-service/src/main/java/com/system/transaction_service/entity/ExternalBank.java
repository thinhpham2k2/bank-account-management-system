package com.system.transaction_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ExternalBank")
@Table(name = "tbl_external_bank")
@EqualsAndHashCode(callSuper = true)
public class ExternalBank extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "code")
    private String code;

    @Column(name = "swift_code")
    private String swiftCode;

    @Column(name = "napas_code")
    private String napasCode;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "logo", length = 4000)
    private String logo;

    @LastModifiedDate
    @Column(name = "date_updated", insertable = false)
    private LocalDateTime date_updated;

    @CreatedBy
    @Column(name = "creator_id", nullable = false, updatable = false)
    private String creatorId;

    @LastModifiedBy
    @Column(name = "updater_id", insertable = false)
    private String updaterId;

    @Column(name = "state")
    private Boolean state;
}
