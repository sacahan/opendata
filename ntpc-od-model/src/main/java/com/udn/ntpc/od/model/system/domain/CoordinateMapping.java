package com.udn.ntpc.od.model.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="st_coordinate_mapping")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
public class CoordinateMapping implements Serializable {

    private static final long serialVersionUID = 3982118162973241041L;

    @Id
	@Column(name = "od_data_cfg_oid", length = 36, nullable = false)
	private String dataCfgOid;
	
	@Column(name = "field_addr", length = 50, nullable = false)
	private String fieldAddr;
	
	@Column(name = "field_name", length = 50, nullable = false)
	private String fieldName;

}
