package com.udn.ntpc.od.model.auth.domain;

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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "auth_unit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"otherMonitorUnits", "monitoredUnits"})
@EqualsAndHashCode(exclude = {"otherMonitorUnits", "monitoredUnits"})
public class AuthUnit implements Serializable {
    private static final long serialVersionUID = 6613323892993120578L;

    //單位為系統最上層的單位時，設這個為"parentUnitID"
    public static final String ROOT_PARENT_ID = "ROOT";

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "guid")
    private String oid; //單位的系統流水號

    @Column(name = "parent_unit_id", length = 100, nullable = false)
    private String parentUnitID; //上級單位UnitID

    @Column(name = "unit_path_name", length = 100, nullable = false)
    private String unitPathName; //上級單位的名稱

    @Column(name = "unit_name", length = 100, nullable = false)
    private String unitName; //單位名稱

    @Column(name = "unit_alias", length = 100, nullable = true)
    private String unitAlias; //單位代稱

    @ManyToMany(cascade = CascadeType.ALL)
    @BatchSize(size = 100)
    @JoinTable(name = "auth_unit_monitor_unit",
            joinColumns = {@JoinColumn(name = "unit_id")},
            inverseJoinColumns = {@JoinColumn(name = "monitor_unit_id")})
    private Set<AuthUnit> otherMonitorUnits = new LinkedHashSet<>(0); //其他管理單位的清單

    @ManyToMany(mappedBy = "otherMonitorUnits")
    @BatchSize(size = 100)
    private Set<AuthUnit> monitoredUnits = new LinkedHashSet<>(0); //被這個單位管的單位清單

    public void removeOtherMonitorUnit(AuthUnit unit) {
        otherMonitorUnits.remove(unit);
        unit.getMonitoredUnits().remove(this);
    }

    public void addOtherMonitorUnit(AuthUnit unit) {
        if (!otherMonitorUnits.contains(unit)) {
            otherMonitorUnits.add(unit);
            unit.getMonitoredUnits().add(this);
        }
    }

    public void removeMonitoredUnit(AuthUnit unit) {
        monitoredUnits.remove(unit);
        unit.getOtherMonitorUnits().remove(this);
    }

    public void addMonitoredUnit(AuthUnit unit) {
        if (!monitoredUnits.contains(unit)) {
            monitoredUnits.add(unit);
            unit.getOtherMonitorUnits().add(this);
        }
    }

}
