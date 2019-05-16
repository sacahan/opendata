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

/**
 * 資料分類詮釋資料歷程的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set_category_ver_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetCategoryVer"})
@EqualsAndHashCode(exclude = {"dataSetCategoryVer"})
public class DataSetCategoryVerMetadata implements Serializable {
    private static final long serialVersionUID = 1097616835243924918L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "metadata_key", nullable = false, columnDefinition = "varchar(max)")
    private String metadataKey;

    @Column(name = "metadata_value", nullable = false, columnDefinition = "varchar(max)")
    private String metadataValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_category_ver_oid", referencedColumnName = "oid", nullable = false)
    private DataSetCategoryVer dataSetCategoryVer;

}
