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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 結構化資料轉入相關資訊
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_table_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfg"})
@EqualsAndHashCode(exclude = {"dataCfg"})
public class DataCfgTableInfo implements Serializable {
    private static final long serialVersionUID = -6238497512546727188L;
    
    @Id
    @Column(name = "oid")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger oid;

    @Column(name = "table_schema")
    private String tableSchema;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "od_data_cfg_oid", insertable = false, updatable = false)
    private String dataCfgOid;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;

    public void setDataCfg(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        if (this.dataCfg == null) {
            this.dataCfgOid = null;
        } else {
            this.dataCfgOid = this.dataCfg.getOid();
        }
    }

}
