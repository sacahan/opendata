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
 * 資料設定版本歷程的標題的PO
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "od_data_set_ver_tag")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSetVer"})
@EqualsAndHashCode(exclude = {"dataSetVer"})
public class DataSetVerTag implements Serializable {
    private static final long serialVersionUID = -9105303690698455752L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
    private String oid;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "od_data_set_ver_oid", referencedColumnName = "oid", nullable = false)
    private DataSetVer dataSetVer;

}
