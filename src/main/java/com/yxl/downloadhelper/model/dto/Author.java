package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Author {
    private UUID id;
    private String name;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
}
