package com.yxl.downloadhelper.common.exception;

public class DataOperationException extends RuntimeException {

    public DataOperationException(String message) {
        super("DataOperationError:"+message);
    }

}
