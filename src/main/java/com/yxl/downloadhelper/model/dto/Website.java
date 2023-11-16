package com.yxl.downloadhelper.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Website {
    private UUID id;
    private String name;
    private String url;
    private JSONObject rule;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
