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
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 手動檔案上傳
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_file")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfg"})
@EqualsAndHashCode(exclude = {"dataCfg"})
public class DataCfgFile implements Serializable {
    private static final long serialVersionUID = 8742398526504867441L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private BigInteger size;

    @Column(name = "content", columnDefinition = "IMAGE")
    @Lob
    private byte[] content;

    @Column(name = "od_data_cfg_oid", insertable = false, updatable = false)
    private String dataCfgOid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid")
    private DataCfg dataCfg;

    public void setDataCfg(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        if (this.dataCfg != null) {
            this.dataCfg.setDataCfgFile(this);
        }
    }

    public <T> T cloneEntity() {
//      return super.cloneEntity();
        DataCfgFile newEntity = new DataCfgFile();
        newEntity.setContent(this.getContent());
        newEntity.setName(this.getName());
        newEntity.setSize(this.getSize());
        return (T) newEntity;
    }

}
