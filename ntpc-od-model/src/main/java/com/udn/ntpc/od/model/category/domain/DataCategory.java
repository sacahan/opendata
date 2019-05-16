package com.udn.ntpc.od.model.category.domain;

import com.udn.ntpc.od.model.set.domain.DataSet;
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="od_data_category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"dataSets", "dataCategoryMetadatas"})
@EqualsAndHashCode(exclude = {"dataSets", "dataCategoryMetadatas"})
public class DataCategory implements Serializable {
    private static final long serialVersionUID = -8415392954859934676L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "guid")
	private String oid;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "icon", columnDefinition = "IMAGE")
	@Lob
	private byte[] icon;

	@OneToMany(mappedBy = "dataCategory")
    @BatchSize(size=100)
	private Set<DataSet> dataSets = new LinkedHashSet<>(0);

	/**
	 * 分類的詮釋資料
	 */
	@OneToMany(mappedBy = "dataCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=100)
	private Set<DataCategoryMetadata> dataCategoryMetadatas = new LinkedHashSet<>(0);

}
	
