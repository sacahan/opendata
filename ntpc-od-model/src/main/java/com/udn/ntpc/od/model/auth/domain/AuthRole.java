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
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "auth_role")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class AuthRole implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "guid")
    private String oid; //角色的系統流水號

    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName; //角色的中文名稱

    @Column(name = "role_level", length = 100)
    private String roleLevel; //角色等級

    @Column(name = "description", length = 100)
    private String description; //角色說明

    @Column(name = "last_edit_user_name", length = 100)
    private String lastEditUserName; //最後修改人

    @Column(name = "last_edit_time")
    private Date lastEditTime; //最後修改時間

//    @Column(name = "is_active", nullable = false)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive; //是否啟用中，預設為是

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "auth_role_function_mapping",
               joinColumns = {@JoinColumn(name = "role_id")},
               inverseJoinColumns = {@JoinColumn(name = "function_id")})
    @BatchSize(size = 100)
    private Set<AuthFunction> functions = new LinkedHashSet<>(0); //可存取的功能清單

    @ManyToMany(mappedBy = "roles")
    @BatchSize(size = 100)
    private Set<AuthUser> users = new LinkedHashSet<>(0); //包含這個role的user們

    public void removeFunction(AuthFunction function) {
        functions.remove(function);
        function.getRoles().remove(this);
    }

    public void addFunction(AuthFunction function) {
        if (!functions.contains(function)) {
            functions.add(function);
            function.getRoles().add(this);
        }
    }

    public void addUser(AuthUser user) {
        if (!users.contains(user)) {
            users.add(user);
            user.getRoles().add(this);
        }
    }

    public void removeUser(AuthUser user) {
        users.remove(user);
        user.getRoles().remove(this);
    }

}
