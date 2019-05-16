package com.udn.ntpc.od.model.set.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * 資料分類歷程的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set_category_ver")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetVer", "dataSetCategoryVerMetadatas"})
@EqualsAndHashCode(exclude = {"dataSetVer", "dataSetCategoryVerMetadatas"})
public class DataSetCategoryVer implements Serializable {
    private static final long serialVersionUID = 2951693912945164457L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "icon", columnDefinition = "IMAGE")
    @Lob
    private byte[] icon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_ver_oid", referencedColumnName = "oid")
    private DataSetVer dataSetVer;

    @OneToMany(mappedBy = "dataSetCategoryVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private List<DataSetCategoryVerMetadata> dataSetCategoryVerMetadatas;

}
