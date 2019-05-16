package com.udn.ntpc.od.model.cfg.domain;

import com.udn.ntpc.od.model.common.ActionType;
import com.udn.ntpc.od.model.common.DataStatus;
import com.udn.ntpc.od.model.set.domain.DataSet;
import com.udn.ntpc.od.model.set.domain.DataSetApply;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 申請用資料設定的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_apply")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true, exclude = {"dataFieldCfgApplies", "dataCfgMetadataApplies", "dataCfgFileApply", "dataCfg", "dataSetApply", "dataSet"})
@EqualsAndHashCode(callSuper = true, exclude = {"dataFieldCfgApplies", "dataCfgMetadataApplies", "dataCfgFileApply", "dataCfg", "dataSetApply", "dataSet"})
public class DataCfgApply extends AbstractDataCfg {
    private static final long serialVersionUID = -2167613827886447846L;

    /**
     * 審核的意見，例如退回的原因
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

    @OneToMany(mappedBy = "dataCfgApply", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    @OrderBy("fieldOrder")
    private Set<DataFieldCfgApply> dataFieldCfgApplies = new LinkedHashSet<>(0);

    @OneToMany(mappedBy = "dataCfgApply", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataCfgMetadataApply> dataCfgMetadataApplies = new LinkedHashSet<>(0);

    /**
     * 手動上傳檔案
     */
    @OneToOne(mappedBy = "dataCfgApply", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private DataCfgFileApply dataCfgFileApply;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid")
    private DataCfg dataCfg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_apply_oid", referencedColumnName = "oid")
    private DataSetApply dataSetApply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid")
    private DataSet dataSet;

/*
    @Override
    @Transient
    public List<? extends IDataFieldCfg> getDataFieldCfgs() {
        return dataFieldCfgApplies;
    }
*/

}
