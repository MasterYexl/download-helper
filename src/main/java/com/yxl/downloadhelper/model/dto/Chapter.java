package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Chapter {
    private UUID id;
    private UUID bookSourceId;
    private int sequence;
    private String url;
    private String title;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
