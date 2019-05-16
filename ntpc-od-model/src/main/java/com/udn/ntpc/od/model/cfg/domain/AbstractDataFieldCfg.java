package com.udn.ntpc.od.model.cfg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 資料設定用的欄位檢核PO
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {/*"dataCfg"*/})
@EqualsAndHashCode(exclude = {/*"dataCfg"*/})
public abstract class AbstractDataFieldCfg implements Serializable, IDataFieldCfg {
    private static final long serialVersionUID = -1285326494158444053L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "field_name", length = 100, nullable = false)
    private String fieldName;

    @Column(name = "disp_name", length = 100)
    private String displayName;

    @Column(name = "field_order", nullable = false)
    private BigInteger fieldOrder;

    @Column(name = "field_type", length = 50)
    private String fieldType;
    
    @Column(name = "field_path", length = 255)
    private String fieldPath;

    @Column(name = "check_method", length = 100)
    private String checkMethod;

    @Column(name = "check_rule", length = 200)
    private String checkRule;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;
*/

    public AbstractDataFieldCfg(String fieldName, String displayName, BigInteger fieldOrder, String fieldType, String checkMethod, String checkRule) {
        super();
        this.fieldName = fieldName;
        this.displayName = displayName;
        this.fieldOrder = fieldOrder;
        this.fieldType = fieldType;
        this.checkMethod = checkMethod;
        this.checkRule = checkRule;
    }


}
