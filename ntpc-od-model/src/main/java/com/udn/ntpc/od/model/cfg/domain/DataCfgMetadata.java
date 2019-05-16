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

/**
 * 資料設定詮釋資料的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfg"})
@EqualsAndHashCode(exclude = {"dataCfg"})
public class DataCfgMetadata implements Serializable {
    private static final long serialVersionUID = 5971300211557587780L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "metadata_key", nullable = false, length = 100)
    private String metadataKey;

    @Column(name = "metadata_value", nullable = false, columnDefinition = "varchar(max)")
    private String metadataValue;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isCommon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;

    /**
     * 設定資料設定
     * 
     * @param dataCfg
     *            資料設定
     */
    public void setDataCfg(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        if (this.dataCfg != null) {
            if (!this.dataCfg.getDataCfgMetadatas().contains(this)) {
                this.dataCfg.getDataCfgMetadatas().add(this);
            }
        }
    }

    public <T> T cloneEntity() {
//      return super.cloneEntity();
        DataCfgMetadata newEntity = new DataCfgMetadata();
        newEntity.setMetadataKey(this.getMetadataKey());
        newEntity.setMetadataValue(this.getMetadataValue());
        newEntity.setCommon(this.isCommon());
        return (T) newEntity;
    }

}
