package com.udn.ntpc.od.model.cfg.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 資料集設定
 */
@MappedSuperclass
@Data
public abstract class AbstractDataCfg implements Serializable, IDataCfg {
    private static final long serialVersionUID = 5146357331898413705L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isStructured;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isEnable;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @Column(name = "no_public_reason")
    private String noPublicReason;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

}
