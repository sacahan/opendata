package com.udn.ntpc.od.model.set.domain;

import com.udn.ntpc.od.model.auth.domain.AuthUnit;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 資料集版本歷程
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set_ver")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetCategoryVer", "dataSetVerTags", "dataSetVerMetadatas", "dataSet"})
@EqualsAndHashCode(exclude = {"dataSetCategoryVer", "dataSetVerTags", "dataSetVerMetadatas", "dataSet"})
public class DataSetVer implements Serializable {
    private static final long serialVersionUID = 611011094949412392L;

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
    private boolean isPublic;

    @Column(name = "no_public_reason")
    private String noPublicReason;

    @Column(name = "edit_user_name", length = 100)
    private String editUserName;

    @Column(name = "public_time")
    private Date publicTime;

    @Column(name = "log_time", nullable = false)
    private Date logTime;

    @OneToOne(mappedBy = "dataSetVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private DataSetCategoryVer dataSetCategoryVer;

    @OneToMany(mappedBy = "dataSetVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @BatchSize(size=100)
    private Set<DataSetVerTag> dataSetVerTags = new LinkedHashSet<>(0);

    @OneToMany(mappedBy = "dataSetVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @BatchSize(size=100)
    private Set<DataSetVerMetadata> dataSetVerMetadatas = new LinkedHashSet<>(0);

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid")
    private DataSet dataSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_oid", referencedColumnName = "oid", nullable = false)
    private AuthUnit unit;

}
