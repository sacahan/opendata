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
@Table(name = "auth_function")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"parentFunctions", "childFunctions", "roles"})
@EqualsAndHashCode(exclude = {"parentFunctions", "childFunctions", "roles"})
public class AuthFunction implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "guid")
    private String oid; //功能的系統流水號

    @Column(name = "function_name", length = 100, nullable = false)
    private String functionName; //功能的中文名稱

/*
    @Column(name = "method_path", length = 100, nullable = false)
    private String methodPath; //功能的class.method名稱
*/

    @Column(name = "access_path", length = 100, nullable = false)
    private String accessPath; //網頁存取url

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isGroup; //是否是功能群組

    @ManyToMany(mappedBy = "childFunctions")
    @BatchSize(size = 100)
    private Set<AuthFunction> parentFunctions = new LinkedHashSet<>(0); //包含這個function的function們

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "auth_function_group",
               joinColumns = {@JoinColumn(name = "function_group_id")},
               inverseJoinColumns = {@JoinColumn(name = "function_id")})
    @BatchSize(size = 100)
    private Set<AuthFunction> childFunctions = new LinkedHashSet<>(0); //是功能群組的話，包含的功能清單

    @ManyToMany(mappedBy = "functions")
    @BatchSize(size = 100)
    private Set<AuthRole> roles = new LinkedHashSet<>(0); //包含這個function的role們

    public void removeParentFunction(AuthFunction function) {
        parentFunctions.remove(function);
        function.getChildFunctions().remove(this);
    }

    public void addParentFunction(AuthFunction function) {
        if (!parentFunctions.contains(function)) {
            parentFunctions.add(function);
            function.getChildFunctions().add(this);
        }
    }

    public void removeChildFunction(AuthFunction function) {
        childFunctions.remove(function);
        function.getParentFunctions().remove(this);
    }

    public void addChildFunction(AuthFunction function) {
        if (!childFunctions.contains(function)) {
            childFunctions.add(function);
            function.getParentFunctions().add(this);
        }
    }

    public void removeRole(AuthRole role) {
        roles.remove(role);
        role.getFunctions().remove(this);
    }

    public void addRole(AuthRole role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.getFunctions().add(this);
        }
    }

}
