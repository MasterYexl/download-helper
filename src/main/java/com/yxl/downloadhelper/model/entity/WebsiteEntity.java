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
@Table(name = "website", schema = "download_helper", catalog = "")
public class WebsiteEntity extends CommonEntity {
    
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "nm")
    private String name;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "rule")
    private String rule;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

}
