package com.udn.ntpc.od.model.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 使用者單位
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ntpc_unit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class NtpcUnit implements Serializable {
    private static final long serialVersionUID = -7136046390966867703L;

    @Id
    @Column(length = 7, nullable = false)
    private String ou;

    @Column(length = 200)
    private String fullName;

    @Column(length = 100)
    private String fullOu;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    private Integer unitLevel;

    @Column(length = 150)
    private String unitName;

    @Column(length = 7)
    private String parentOu;

}
