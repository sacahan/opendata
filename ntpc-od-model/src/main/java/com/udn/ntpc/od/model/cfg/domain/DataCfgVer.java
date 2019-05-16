package com.udn.ntpc.od.model.cfg.domain;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 資料設定版本歷程的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_ver")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataFieldCfgVers", "dataCfgVerMetadatas", "dataCfg"})
@EqualsAndHashCode(exclude = {"dataFieldCfgVers", "dataCfgVerMetadatas", "dataCfg"})
public class DataCfgVer implements Serializable {
    private static final long serialVersionUID = 4167054881823373752L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "data_set_name", length = 100)
    private String dataSetName;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isStructured;

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

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "log_time", nullable = false)
    private Date logTime;

    @OneToMany(mappedBy = "dataCfgVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    @OrderBy("fieldOrder")
    private Set<DataFieldCfgVer> dataFieldCfgVers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "dataCfgVer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
    private Set<DataCfgVerMetadata> dataCfgVerMetadatas = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_oid", referencedColumnName = "oid", nullable = false)
    private DataCfg dataCfg;

}
