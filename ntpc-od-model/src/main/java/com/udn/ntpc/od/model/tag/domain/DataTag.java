package com.udn.ntpc.od.model.tag.domain;

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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="od_data_tag")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {/*"dataSets"*/})
@EqualsAndHashCode(exclude = {/*"dataSets"*/})
public class DataTag implements Serializable {
    private static final long serialVersionUID = -6489678324478657833L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="guid")
	private String oid;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "remark", nullable = true, columnDefinition = "nvarchar(max)")
	private String remark;

	/**
	 * 標籤訪問次數，標籤雲會用到
	 */
	@Column(name = "access_count")
	private int accessCount;

/*
	@ManyToMany(fetch = FetchType.LAZY)
    @BatchSize(size=100)
	@JoinTable(name = "od_data_set_tag", joinColumns = @JoinColumn(name = "od_data_tag_oid"), inverseJoinColumns = @JoinColumn(name = "od_data_set_oid"))
	private Set<DataSet> dataSets = new LinkedHashSet<>(0);
*/

	public void addAccessCount() {
		this.accessCount += 1;
	}

}
