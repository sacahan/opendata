package com.udn.ntpc.od.model.set.domain;

import com.udn.ntpc.od.model.auth.domain.AuthUnit;
import com.udn.ntpc.od.model.category.domain.DataCategory;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.common.Metadata;
import com.udn.ntpc.od.model.tag.domain.DataTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetMetadatas", "dataCategory", "dataTags", "dataCfg", /*"dataCfgApply", "dataSetApply",*/ "unit", "parentDataSet", "childDataSets"})
@EqualsAndHashCode(exclude = {"dataSetMetadatas", "dataCategory", "dataTags", "dataCfg", /*"dataCfgApply", "dataSetApply",*/ "unit", "parentDataSet", "childDataSets"})
public class DataSet implements Serializable {
    private static final long serialVersionUID = -3391332445279938695L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isEnable;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isApplied;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @Column(name = "no_public_reason")
    private String noPublicReason;

    @Column(name = "public_time")
    private Date publicTime;

    @Column(name = "last_edit_user_name", length = 100)
    private String lastEditUserName;

    @Column(name = "last_edit_time")
    private Date lastEditTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "data_out_count", nullable = false, columnDefinition = "bigint default '0'")
    private long dataOutCount;

    @Column(name = "download_count", nullable = false, columnDefinition = "bigint default '0'")
    private long downloadCount;

    @Column(name = "view_count", nullable = false, columnDefinition = "bigint default '0'")
    private long viewCount;

    @Column(name = "data_srv_count", nullable = false, columnDefinition = "bigint default '0'")
    private long dataSrvCount;

    @OneToMany(mappedBy = "dataSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataSetMetadata> dataSetMetadatas = new LinkedHashSet<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_category_oid", referencedColumnName = "oid")
    private DataCategory dataCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @BatchSize(size=100)
    @JoinTable(name = "od_data_set_tag", joinColumns = @JoinColumn(name = "od_data_set_oid"), inverseJoinColumns = @JoinColumn(name = "od_data_tag_oid"))
    private Set<DataTag> dataTags = new LinkedHashSet<>(0);

    @OneToOne(mappedBy = "dataSet", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = false)
    private DataCfg dataCfg;

/*
    @OneToOne(mappedBy = "dataSet", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private DataCfgApply dataCfgApply;

    @OneToOne(mappedBy = "dataSet", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private DataSetApply dataSetApply;
*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_oid", referencedColumnName = "oid", nullable = false)
    private AuthUnit unit;

    @Column(name = "parent_oid", length = 36, insertable = false, updatable = false)
    private String parentOid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_oid", referencedColumnName = "oid")
    private DataSet parentDataSet;
    
    @OneToMany(mappedBy = "parentDataSet", fetch = FetchType.LAZY)
    @BatchSize(size=100)
    @OrderBy("name")
    private Set<DataSet> childDataSets = new LinkedHashSet<>(0);

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        if (isPublic) {
            setPublicTime(new Date());
        }
    }

/*
    public void addDataTagPoList(List<DataTagPo> dataTagPoList) {
        this.dataTagPoList.addAll(dataTagPoList);
    }

    public void addDataOutCount() {
        this.dataOutCount++;
    }

    public void addDownloadCount() {
        this.downloadCount++;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addDataSrvCount() {
        this.dataSrvCount++;
    }
*/
    public void setParentDataSet(DataSet parentDataSet) {
        this.parentDataSet = parentDataSet;
        if (this.parentDataSet == null) {
            this.parentOid = null;
        } else {
            this.parentOid = this.parentDataSet.getOid();
            this.parentDataSet.childDataSets.add(this);
        }
    }

    public boolean isParent() {
        if (StringUtils.isNotBlank(parentOid) || parentDataSet != null) {
            return false;
        }

        if (dataCfg == null) {
            return false;
        }
        
        String groupingFields = Metadata.getGroupingFields(dataCfg);
        return StringUtils.isNotBlank(groupingFields);
    }

    public boolean isChild() {
        if (StringUtils.isBlank(parentOid) || parentDataSet == null) {
            return false;
        }

        if (dataCfg == null) {
            return false;
        }
        
        String groupingFields = Metadata.getGroupingFields(dataCfg);
        String groupingValue = Metadata.getGroupingValue(dataCfg);
        return (StringUtils.isNotBlank(groupingFields) && StringUtils.isBlank(groupingValue));
    }
    
    public boolean isKml() {
        if (dataCfg == null) {
            return false;
        }
        
        return Metadata.isKmlDataSet(dataCfg);
    }

    // For grouping
    public <T> T cloneEntity() {
//        return super.cloneEntity();
        Date now = new Date();
        DataSet newEntity = new DataSet();

        newEntity.setCreateTime(now);
        newEntity.setDescription(this.getDescription());
        newEntity.setActive(this.isActive());
        newEntity.setApplied(this.isApplied());
        newEntity.setEnable(this.isEnable());
        newEntity.setPublic(this.isPublic());
        newEntity.setLastEditTime(now);
        newEntity.setLastEditUserName(this.getLastEditUserName());
        newEntity.setName(this.getName());
        newEntity.setPublicTime(now);
        newEntity.setDataCategory(this.getDataCategory());
        newEntity.setUnit(this.getUnit());

        //設定標籤
        for (DataTag dataTag : this.getDataTags()) {
            newEntity.getDataTags().add(dataTag);
        }

        //設定metadata
        for (DataSetMetadata currentDataSetMetadata: this.getDataSetMetadatas()) {
            DataSetMetadata newDataSetMetadata = currentDataSetMetadata.cloneEntity();
            newDataSetMetadata.setDataSet(newEntity);
        }

        return (T) newEntity;
    }

    public void copyFrom(DataSet sourceDataSet) {
        Date now = new Date();
        DataSet currentEntity = this;

        // currentEntity.setCreateTime(sourceDataSet.createTime);
        currentEntity.setDescription(sourceDataSet.getDescription());
        currentEntity.setActive(sourceDataSet.isActive());
        currentEntity.setApplied(sourceDataSet.isApplied());
        currentEntity.setEnable(sourceDataSet.isEnable());
        currentEntity.setPublic(sourceDataSet.isPublic());
        currentEntity.setLastEditTime(now);
        currentEntity.setLastEditUserName(sourceDataSet.getLastEditUserName());
        currentEntity.setName(sourceDataSet.getName());
        // currentEntity.setPublicTime(now);
        currentEntity.setDataCategory(sourceDataSet.getDataCategory());
        currentEntity.setUnit(sourceDataSet.getUnit());

        // 設定標籤
        currentEntity.getDataTags().clear();
        for (DataTag dataTag : sourceDataSet.getDataTags()) {
            currentEntity.getDataTags().add(dataTag);
        }

        // 設定metadata, 保留原始 identifier
        String identifier = Metadata.getIdentifier(currentEntity);
        currentEntity.getDataSetMetadatas().clear();
        for (DataSetMetadata currentDataSetMetadata : sourceDataSet.getDataSetMetadatas()) {
            DataSetMetadata newDataSetMetadata = currentDataSetMetadata.cloneEntity();
            newDataSetMetadata.setDataSet(currentEntity);
        }
        Metadata.setIdentifier(currentEntity, identifier);
    }

}
