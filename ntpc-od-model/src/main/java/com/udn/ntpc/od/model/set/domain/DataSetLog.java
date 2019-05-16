package com.udn.ntpc.od.model.set.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 記錄資料集變更歷程
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="od_data_set_log")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSet"})
@EqualsAndHashCode(exclude = {"dataSet"})
public class DataSetLog implements Serializable {
    private static final long serialVersionUID = -581204814410986549L;

    @Id
    @Column(name = "oid")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;
	
    @Column(name="od_data_set_oid", length = 36, nullable = false)
    private String dataSetOid;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "od_data_set_oid", referencedColumnName = "oid", insertable = false, updatable = false)
    private DataSet dataSet;
    
	@Column(name="perform_action", length = 50, nullable = false)
	private String performAction;
	
	@Column(name="od_data_set_name", length = 100)
	private String dataSetName;
	
    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;

    public DataSetLog(int oid, String dataSetOid, String performAction, String dataSetName, Date createDate, DataSet dataSet) {
        this.oid = oid;
        this.dataSetOid = dataSetOid;
        this.performAction = performAction;
        this.dataSetName = dataSetName;
        this.createDate = createDate;
        this.dataSet = dataSet;
    }

}
