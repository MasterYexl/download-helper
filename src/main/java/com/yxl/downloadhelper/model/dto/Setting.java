package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Setting {
    private UUID id;
    private UUID usrId;
    private String backgroundColor;
    private String fontColor;
    private Integer fontSize;
    private Timestamp createTime;
    private Timestamp updateTime;
}

