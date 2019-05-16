package com.udn.ntpc.od.model.cfg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 壓縮檔資訊
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_zip_file")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfg"})
@EqualsAndHashCode(exclude = {"dataCfg"})
public class DataCfgZipFile implements Serializable {
    private static final long serialVersionUID = -5556081145531743138L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "file_name", nullable = false)
    private String zipFileName;

    /**
     * 檔案類型(副檔名)
     */
    @Column(name = "source_type", length = 36, nullable = false)
    private String sourceType;

    @Column(name = "size", nullable = false)
    private BigInteger size;

    @Column(name = "content_file", columnDefinition = "IMAGE")
    @Lob
    private byte[] contentFile;

    @Column(name = "md5", length = 36, nullable = false)
    private String md5;

    @Column(name = "od_data_cfg_oid", insertable = false, updatable = false)
    private String dataCfgOid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;


    public void setContentFile(File contentFile) throws IOException {
        try(BufferedInputStream inputStream = IOUtils.buffer(new FileInputStream(contentFile));) {
            this.contentFile = IOUtils.toByteArray(inputStream);
            this.setContentFile(this.contentFile);
        }
    }
    
    public void setContentFile(byte[] contentFile) {
        this.contentFile = contentFile;
        this.size = BigInteger.valueOf(this.contentFile.length);
    }

    /**
     * 設定 所屬的資料設定
     * 
     * @param dataCfg
     *            資料設定
     */
    public void setDataCfg(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        if (this.dataCfg != null) {
            this.dataCfgOid = this.dataCfg.getOid();
            this.dataCfg.getDataCfgZipFiles().add(this);
        }
    }

}
