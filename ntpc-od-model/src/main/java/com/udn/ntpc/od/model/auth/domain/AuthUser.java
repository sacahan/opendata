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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "auth_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 6113386894463858022L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "guid")
    private String oid; //使用者的系統流水號

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private AuthUnit unit; //所屬單位

    @Column(name = "user_name", nullable = false, columnDefinition = "nvarchar(100)")
    private String userName; //使用者姓名

    @Column(name = "password", length = 100, nullable = false)
    private String password; //密碼

    @Column(name = "email", length = 100, nullable = false)
    private String email; //聯絡email

    @Column(name = "phone", nullable = false, columnDefinition = "nvarchar(36) default ''")
    private String phone; //聯絡電話

    @Column(name = "login_id", length = 100, nullable = false)
    private String loginId; //使用者帳號

//    @Column(name = "is_active", nullable = false)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isActive = true; //是否啟用中，預設為是

    // 建立時間（允許 null）
    @Column(name = "create_time", nullable = true)
    private Date createTime;

    // 最後修改時間（允許 null）
    @Column(name = "last_update_time", nullable = true)
    private Date lastUpdateTime;

    @ManyToMany(cascade = CascadeType.ALL)
    @BatchSize(size = 100)
    @JoinTable(name = "auth_user_role_mapping",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<AuthRole> roles = new LinkedHashSet<>(); //所屬的角色的清單

/*
    public void removeRole(AuthRole role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public void addRole(AuthRole role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.getUsers().add(this);
        }
    }
*/

}
