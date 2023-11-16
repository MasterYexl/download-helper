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
@Table(name = "setting", schema = "download_helper", catalog = "")
public class SettingEntity extends CommonEntity {
    
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "usr_id")
    private UUID usrId;
    @Basic
    @Column(name = "background_color")
    private String backgroundColor;
    @Basic
    @Column(name = "font_color")
    private String fontColor;
    @Basic
    @Column(name = "font_size")
    private Integer fontSize;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;

}
