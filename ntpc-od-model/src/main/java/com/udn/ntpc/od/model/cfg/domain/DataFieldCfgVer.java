package com.udn.ntpc.od.model.cfg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 資料設定版本歷程用的欄位檢核PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_field_cfg_ver")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfgVer"})
@EqualsAndHashCode(exclude = {"dataCfgVer"})
public class DataFieldCfgVer implements Serializable {
    private static final long serialVersionUID = 1269675957076204499L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "field_name", length = 50, nullable = false)
    private String fieldName;

    @Column(name = "disp_name", length = 100)
    private String dispName;

    @Column(name = "field_order", nullable = false)
    private BigInteger fieldOrder;

    @Column(name = "field_type", length = 50)
    private String fieldType;

    @Column(name = "check_method", length = 100)
    private String checkMethod;

    @Column(name = "check_rule", length = 100)
    private String checkRule;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_ver_oid", referencedColumnName = "oid", nullable = false)
    private DataCfgVer dataCfgVer;

}
