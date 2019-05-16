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
 * 資料設定用的欄位檢核PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_field_cfg")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true, exclude = {"dataCfg"})
@EqualsAndHashCode(callSuper = true, exclude = {"dataCfg"})
public class DataFieldCfg extends AbstractDataFieldCfg {
    private static final long serialVersionUID = 87676855398906062L;

    @Column(name="od_data_cfg_oid", length = 36, nullable = false, insertable = false, updatable = false)
    private String dataCfgOid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;

    public DataFieldCfg(String fieldName, String displayName, BigInteger fieldOrder, String fieldType, String checkMethod, String checkRule) {
        super(fieldName, displayName, fieldOrder, fieldType, checkMethod, checkRule);
    }

    public IDataCfg getDataCfg() {
        return dataCfg;
    }

    public void setDataCfg(IDataCfg dataCfg) {
        this.dataCfg = (DataCfg) dataCfg;
        if (this.dataCfg != null) {
            this.dataCfg.getDataFieldCfgs().add(this);
        }
    }

    public <T> T cloneEntity() {
//      return super.cloneEntity();
        DataFieldCfg newEntity = new DataFieldCfg();
        newEntity.setFieldName(this.getFieldName());
        newEntity.setDisplayName(this.getDisplayName());
        newEntity.setFieldOrder(this.getFieldOrder());
        newEntity.setFieldType(this.getFieldType());
        newEntity.setCheckRule(this.getCheckRule());
        newEntity.setCheckMethod(this.getCheckMethod());
        newEntity.setPublic(this.isPublic());
        return (T) newEntity;
    }

}
