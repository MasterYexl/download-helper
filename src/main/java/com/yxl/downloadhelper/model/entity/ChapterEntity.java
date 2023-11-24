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
@Table(name = "chapter", schema = "download_helper", catalog = "")
public class ChapterEntity extends CommonEntity {
    
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "book_source_id")
    private UUID bookSourceId;
    @Basic
    @Column(name = "seq")
    private int sequence;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Basic
    @Column(name = "url")
    private String url;
}
