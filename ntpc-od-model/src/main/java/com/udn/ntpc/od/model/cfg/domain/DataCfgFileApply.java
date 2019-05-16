package com.udn.ntpc.od.model.cfg.domain;

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
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 申請夾帶檔案的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_cfg_file_apply")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataCfgApply"})
@EqualsAndHashCode(exclude = {"dataCfgApply"})
public class DataCfgFileApply implements Serializable {
    private static final long serialVersionUID = -4704530421351914660L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private BigInteger size;

    @Column(name = "content", columnDefinition = "IMAGE")
    @Lob
    private byte[] content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_cfg_apply_oid", referencedColumnName = "oid")
    private DataCfgApply dataCfgApply;

}
