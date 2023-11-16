package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Content {
    private UUID id;
    private UUID chapterId;
    private Integer sequence;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
