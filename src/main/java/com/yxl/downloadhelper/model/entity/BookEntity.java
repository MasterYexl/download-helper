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
@Table(name = "book", schema = "download_helper", catalog = "")
public class BookEntity extends CommonEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "nm")
    private String name;
    @Basic
    @Column(name = "author_id")
    private UUID authorId;
    @Basic
    @Column(name = "series_id")
    private UUID seriesId;
    @Basic
    @Column(name = "series_sequence")
    private Integer seriesSequence;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time")
    private Timestamp updateTime;
}
    
