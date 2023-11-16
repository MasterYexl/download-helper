package com.yxl.downloadhelper.common.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
@Slf4j
public class ResponseTemplate<T extends Serializable> implements Serializable {

    private T detail;

    private Pageable pageable;

    private int totalPages;

    private long totalElements;

    private ResponseState state;

    private String template;

    private String message;

    private String url;

    public static <T extends Serializable>ResponseTemplate<T> success(T detail) {
        return success(detail, null);
    }

    public static <T extends Serializable>ResponseTemplate<T> success(T detail, Pageable pageable, int totalPages, long totalElements) {
        ResponseTemplate<T> success = success(detail, null);
        success.setPageable(pageable);
        success.setTotalPages(totalPages);
        success.setTotalElements(totalElements);
        return success;
    }

    public static <T extends Serializable>ResponseTemplate<T> success(T detail, String template) {
        ResponseTemplate<T> commonDTO = new ResponseTemplate<>();
        commonDTO.setState(ResponseState.Success);
        commonDTO.setDetail(detail);
        commonDTO.setTemplate(template);
        return commonDTO;
    }
    public static <T extends Serializable>ResponseTemplate<T> fail(String message) {
        log.debug("request fail:"+message);
        return fail(message, null);
    }
    public static <T extends Serializable>ResponseTemplate<T> fail(String message, String template) {
        ResponseTemplate<T> commonDTO = new ResponseTemplate<>();
        commonDTO.setState(ResponseState.Fail);
        commonDTO.setMessage(message);
        commonDTO.setTemplate(template);
        return commonDTO;
    }
    public boolean isSuccess() {
        return state == ResponseState.Success;
    }
}
