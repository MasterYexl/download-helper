package com.yxl.downloadhelper.model.entity;

import com.yxl.downloadhelper.common.model.entity.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "usr", schema = "download_helper", catalog = "")
public class UserEntity extends CommonEntity {
    
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "nm")
    private String name;
    @Basic
    @Column(name = "pswd")
    private String password;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

}
