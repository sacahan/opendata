package com.udn.ntpc.od.model.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="st_sys_param")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
public class SysParam implements Serializable {
    private static final long serialVersionUID = 5395603013314812753L;

    @Id
	@Column(name="id", length = 100, nullable = false)
	private String id;
	
	@Column(name="name", length = 100, nullable = false)
	private String name;
	
	@Column(name="note", length = 300, nullable = false)
	private String note;
	
	@Column(name="val", length = 300)
	private String val;
	
	@Column(name = "disp_sort", nullable = false)
	private Integer dispSort;
	
	@Column(name = "update_date", nullable = false)
	@LastModifiedDate
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date updateDate;

}
