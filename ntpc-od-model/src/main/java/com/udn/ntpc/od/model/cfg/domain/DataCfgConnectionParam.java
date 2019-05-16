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

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="od_data_cfg_connection_param")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfg"})
@EqualsAndHashCode(exclude = {"dataCfg"})
public class DataCfgConnectionParam implements Serializable {
    private static final long serialVersionUID = -2098634098205639113L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;
	
	@Column(name="param_key", length = 100, nullable = false)
	private String paramKey;
	
	@Column(name="param_value", length = 300, nullable = false)
	private String paramValue;
	
	@Column(name="od_data_cfg_oid", length = 36, nullable = false)
	private String dataCfgOid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", insertable = false, updatable = false)
    private DataCfg dataCfg;

    public DataCfgConnectionParam(String paramKey, String paramValue) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }

    public DataCfgConnectionParam(DataCfg dataCfg, String paramKey, String paramValue) {
        setDataCfg(dataCfg);
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }

    public void setDataCfg(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        if (this.dataCfg != null) {
            this.dataCfgOid = this.dataCfg.getOid();
        }
    }

}
