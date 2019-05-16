package com.udn.ntpc.od.model.set.domain;

import com.udn.ntpc.od.model.auth.domain.AuthUnit;
import com.udn.ntpc.od.model.category.domain.DataCategory;
import com.udn.ntpc.od.model.cfg.domain.DataCfgApply;
import com.udn.ntpc.od.model.common.ActionType;
import com.udn.ntpc.od.model.common.DataStatus;
import com.udn.ntpc.od.model.tag.domain.DataTag;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 申請用資料集的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set_apply")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetMetadataApplies", "dataCategory", "dataTags", "dataCfgApply", "dataSet", "unit"})
@EqualsAndHashCode(exclude = {"dataSetMetadataApplies", "dataCategory", "dataTags", "dataCfgApply", "dataSet", "unit"})
public class DataSetApply implements Serializable {
    private static final long serialVersionUID = 2655339553573435621L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isEnable;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @Column(name = "no_public_reason")
    private String noPublicReason;

    /**
     * 取得審核的意見，例如退回的原因
     */
    @Column(name = "comment", columnDefinition = "varchar(max)")
    private String comment;

    @Column(name = "apply_user_name", length = 100, nullable = false)
    private String applyUserName;

    @Column(name = "apply_time", nullable = false)
    private Date applyTime;

    @Column(name = "refuse_time")
    private Date refuseTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "data_status", nullable = false)
    private DataStatus dataStatus;

    @OneToMany(mappedBy = "dataSetApply", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @BatchSize(size=100)
    private Set<DataSetMetadataApply> dataSetMetadataApplies = new LinkedHashSet<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_category_oid", referencedColumnName = "oid")
    private DataCategory dataCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @BatchSize(size=100)
    @JoinTable(name = "od_data_set_apply_tag", joinColumns = @JoinColumn(name = "od_data_set_apply_oid"), inverseJoinColumns = @JoinColumn(name = "od_data_tag_oid"))
    private Set<DataTag> dataTags = new LinkedHashSet<>(0);

    @OneToOne(mappedBy = "dataSetApply", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = false)
    private DataCfgApply dataCfgApply;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid")
    private DataSet dataSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_oid", referencedColumnName = "oid", nullable = false)
    private AuthUnit unit;

}
