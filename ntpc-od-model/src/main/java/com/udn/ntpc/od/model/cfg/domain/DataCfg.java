package com.udn.ntpc.od.model.cfg.domain;

import com.udn.ntpc.od.model.common.Metadata;
import com.udn.ntpc.od.model.set.domain.DataSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 資料設定的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true, exclude = {"dataFieldCfgs", "dataCfgMetadatas", "dataCfgFile", "dataCfgTableInfo", "dataSet", /*"dataCfgApply",*/ "dataCfgZipFiles", "dataCfgConnectionParams"})
@EqualsAndHashCode(callSuper = true, exclude = {"dataFieldCfgs", "dataCfgMetadatas", "dataCfgFile", "dataCfgTableInfo", "dataSet", /*"dataCfgApply",*/ "dataCfgZipFiles", "dataCfgConnectionParams"})
public class DataCfg extends AbstractDataCfg {
    private static final long serialVersionUID = -8808381573942735384L;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isApplied;

    @Column(name = "public_time")
    private Date publicTime;

    @Column(name = "last_edit_user_name", length = 100)
    private String lastEditUserName;

    @Column(name = "last_edit_time")
    private Date lastEditTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "data_count")
    private int dataCount = 0;

    @OneToMany(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    @OrderBy("fieldOrder")
    private Set<DataFieldCfg> dataFieldCfgs = new LinkedHashSet<>(0);

    @OneToMany(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataCfgMetadata> dataCfgMetadatas = new LinkedHashSet<>(0);

    /**
     * 手動上傳檔案
     */
    @OneToOne(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private DataCfgFile dataCfgFile;

    @OneToOne(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private DataCfgTableInfo dataCfgTableInfo;

    @Column(name = "od_data_set_oid", insertable = false, updatable = false)
    private String dataSetOid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid")
    private DataSet dataSet;

/*
    @OneToOne(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private DataCfgApply dataCfgApply;
*/

    /**
     * 打包過的檔案，壓縮相關資訊
     */
    @OneToMany(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataCfgZipFile> dataCfgZipFiles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "dataCfg", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataCfgConnectionParam> dataCfgConnectionParams = new LinkedHashSet<>();

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        if (this.dataSet != null) {
            this.dataSet.setDataCfg(this);
        }
    }

/*
    public void addDataCfgZipFile(DataCfgZipFile dataCfgZipFile) {
        dataCfgZipFiles.add(dataCfgZipFile);
    }
*/

/*
    @Override
    @Transient
    public List<? extends IDataFieldCfg> getDataFieldCfgs() {
        DataFieldCfgs dataFieldCfgs = new DataFieldCfgs();
        dataFieldCfgs.addAll(this.dataFieldCfgs);
        return dataFieldCfgs;
    }
*/

    public <T> T cloneEntity() {
//      return super.cloneEntity();
        Date now = new Date();

        DataCfg newEntity = new DataCfg();
        newEntity.setCreateTime(now);
        newEntity.setDescription(this.getDescription());
        newEntity.setEndTime(this.getEndTime());
        newEntity.setActive(this.isActive());
        newEntity.setApplied(this.isApplied());
        newEntity.setEnable(this.isEnable());
        newEntity.setPublic(this.isPublic());
        newEntity.setStructured(this.isStructured());
        newEntity.setLastEditTime(now);
        newEntity.setLastEditUserName(this.getLastEditUserName());
        newEntity.setName(this.getName());
        newEntity.setNoPublicReason(this.getNoPublicReason());
        newEntity.setPublicTime(this.getPublicTime());
        newEntity.setStartTime(this.getStartTime());
        newEntity.setUpdateTime(now);

        // 檔案
        if (this.getDataCfgFile() != null) {
            DataCfgFile newDataCfgFile = this.getDataCfgFile().cloneEntity();
            newDataCfgFile.setDataCfg(newEntity);
        }

        //設定欄位檢核
        for (DataFieldCfg currentDataFieldCfg : this.getDataFieldCfgs()) {
            DataFieldCfg newDataFieldCfg = currentDataFieldCfg.cloneEntity();
            newDataFieldCfg.setDataCfg(newEntity);
        }

        //設定metadata
        for (DataCfgMetadata currentDataCfgMetadata : this.getDataCfgMetadatas()) {
            DataCfgMetadata newDataCfgMetadata = currentDataCfgMetadata.cloneEntity();
            newDataCfgMetadata.setDataCfg(newEntity);
        }

        return (T) newEntity;
    }

    public void copyFrom(DataCfg sourceDataCfg) {
//      return super.cloneEntity();
        Date now = new Date();

        DataCfg currentEntity = this;
//        newEntity.setCreateTime(now);
        currentEntity.setDescription(sourceDataCfg.getDescription());
        currentEntity.setEndTime(sourceDataCfg.getEndTime());
        currentEntity.setActive(sourceDataCfg.isActive());
        currentEntity.setApplied(sourceDataCfg.isApplied());
        currentEntity.setEnable(sourceDataCfg.isEnable());
        currentEntity.setPublic(sourceDataCfg.isPublic());
        currentEntity.setStructured(sourceDataCfg.isStructured());
        currentEntity.setLastEditTime(now);
        currentEntity.setLastEditUserName(sourceDataCfg.getLastEditUserName());
        currentEntity.setName(sourceDataCfg.getName());
        currentEntity.setNoPublicReason(sourceDataCfg.getNoPublicReason());
        currentEntity.setPublicTime(sourceDataCfg.getPublicTime());
        currentEntity.setStartTime(sourceDataCfg.getStartTime());
        currentEntity.setUpdateTime(now);

        // 檔案
        if (sourceDataCfg.getDataCfgFile() != null) {
            DataCfgFile newDataCfgFile = sourceDataCfg.getDataCfgFile().cloneEntity();
            newDataCfgFile.setDataCfg(currentEntity);
        }

        //設定欄位檢核
        currentEntity.getDataFieldCfgs().clear();
        for (DataFieldCfg currentDataFieldCfg : sourceDataCfg.getDataFieldCfgs()) {
            DataFieldCfg newDataFieldCfg = currentDataFieldCfg.cloneEntity();
            newDataFieldCfg.setDataCfg(currentEntity);
        }

        //設定metadata, 保留原始 resourceId & subResourceIds
        String resourceId = Metadata.getResourceId(currentEntity);
        String subResourceIds = Metadata.getSubResourceIds(currentEntity);
        currentEntity.getDataCfgMetadatas().clear();
        for (DataCfgMetadata currentDataCfgMetadata : sourceDataCfg.getDataCfgMetadatas()) {
            DataCfgMetadata newDataCfgMetadata = currentDataCfgMetadata.cloneEntity();
            newDataCfgMetadata.setDataCfg(currentEntity);
        }
        Metadata.setResourceId(currentEntity, resourceId);
        Metadata.setSubResourceIds(currentEntity, subResourceIds);
    }

}
