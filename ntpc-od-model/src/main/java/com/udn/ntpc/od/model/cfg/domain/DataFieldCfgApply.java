package com.udn.ntpc.od.model.cfg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigInteger;

/**
 * 資料設定申請用的欄位檢核PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_field_cfg_apply")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true, exclude = {"dataCfgApply"})
@EqualsAndHashCode(callSuper = true, exclude = {"dataCfgApply"})
public class DataFieldCfgApply extends AbstractDataFieldCfg {
    private static final long serialVersionUID = 5641925793752138882L;

    @Column(name="od_data_cfg_apply_oid", length = 36, nullable = false, insertable = false, updatable = false)
    private String dataCfgOid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_apply_oid", referencedColumnName = "oid", nullable = false)
    private DataCfgApply dataCfgApply;

    public DataFieldCfgApply(String fieldName, String displayName, BigInteger fieldOrder, String fieldType, String checkMethod, String checkRule) {
        super(fieldName, displayName, fieldOrder, fieldType, checkMethod, checkRule);
    }

    public IDataCfg getDataCfg() {
        return dataCfgApply;
    }

    public void setDataCfg(IDataCfg dataCfg) {
        this.dataCfgApply = (DataCfgApply) dataCfg;
        if (this.dataCfgApply != null) {
            this.dataCfgApply.getDataFieldCfgApplies().add(this);
        }
    }

}
