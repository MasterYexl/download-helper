package com.yxl.downloadhelper.common.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public abstract class CommonEntity implements Serializable {

    public CommonEntity(UUID id) {
        setId(id);
    }

    public CommonEntity() {

    }

    public abstract UUID getId();

    public abstract void setId(UUID id);

    public abstract Timestamp getCreateTime();
    public abstract void setCreateTime(Timestamp time);
    public abstract Timestamp getUpdateTime();
    public abstract void setUpdateTime(Timestamp time);

}
