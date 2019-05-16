package com.udn.ntpc.od.model.set.domain;

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
@Table(name = "od_data_set_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSet"})
@EqualsAndHashCode(exclude = {"dataSet"})
public class DataSetMetadata implements Serializable {
    private static final long serialVersionUID = 8486117009556400657L;

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
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid", nullable = false)
    private DataSet dataSet;

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        if (this.dataSet != null) {
            if (!this.dataSet.getDataSetMetadatas().contains(this)) {
                this.dataSet.getDataSetMetadatas().add(this);
            }
        }
    }

    // for grouping
    public <T> T cloneEntity() {
//        return super.cloneEntity();
        DataSetMetadata newEntity = new DataSetMetadata();
        newEntity.setCommon(this.isCommon());
        newEntity.setMetadataKey(this.getMetadataKey());
        newEntity.setMetadataValue(this.getMetadataValue());
        return (T) newEntity;
    }

}
