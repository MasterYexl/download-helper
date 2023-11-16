package com.yxl.downloadhelper.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonDTO implements Serializable {

    public String errorMessage;

    public ResponseState responseState;

    public Serializable detail;



}
