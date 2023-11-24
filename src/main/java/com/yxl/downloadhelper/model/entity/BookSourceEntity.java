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
@Table(name = "book_source", schema = "download_helper", catalog = "")
public class BookSourceEntity extends CommonEntity {
    
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "book_id")
    private UUID bookId;
    @Basic
    @Column(name = "website_id")
    private UUID websiteId;
    @Basic
    @Column(name = "new_seq")
    private Integer newSequence;
    @Basic
    @Column(name = "new_chapter")
    private String newChapter;
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
