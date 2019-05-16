package com.udn.ntpc.od.frontend.entity.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "MOCK_USER")
public class MockUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"USER_ID\"")
    private Integer userId;

    @Column(name = "\"NAME\"", columnDefinition = "nvarchar(50) not null")
    private String name;

    @Column(name = "\"CITY\"", columnDefinition = "nvarchar(100)")
    private String city;

    @Column(name = "\"EMAIL\"", columnDefinition = "nvarchar(200)")
    private String email;

    @Column(name = "\"GENDER\"", columnDefinition = "char(1)      not null")
    private String gender;

    @Column(name = "\"BIRTHDAY\"", columnDefinition = "date         not null")
    private Date birthday;

    @Column(name = "\"NOTE\"", columnDefinition = "nvarchar(50) not null")
    private String note;

    @Column(name = "\"STATUS\"", columnDefinition = "nvarchar(500)")
    private String status;

    @CreatedBy
    @Column(name = "\"CREATE_USER\"", columnDefinition = "nvarchar(30)")
    private String createUser;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "\"CREATE_TIME\"", columnDefinition = "datetime")
    private Date createTime;

    @LastModifiedBy
    @Column(name = "\"UPDATE_USER\"", columnDefinition = "nvarchar(30)")
    private String updateUser;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "\"UPDATE_TIME\"", columnDefinition = "datetime")
    private Date updateTime;
}