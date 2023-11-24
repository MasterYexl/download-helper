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
@Table(name = "book_shelf", schema = "download_helper", catalog = "")
public class BookShelfEntity extends CommonEntity {

    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "usr_id")
    private UUID usrId;
    @Basic
    @Column(name = "book_source_id")
    private UUID bookSourceId;
    @Basic
    @Column(name = "reading_book_seq")
    private Integer readingBookSequence;
    @Basic
    @Column(name = "chapter_progress")
    private Double chapterProgress;
    @Basic
    @Column(name = "on_shelf")
    private byte onShelf;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;
}
    
