package com.property.management.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    
    private String message;
    
    private T data;
    
    public ResponseResult() {
        this.code = 200;
        this.message = "操作成功";
    }
    
    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>();
    }
    
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }
    
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }
    
    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message);
    }
    
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message);
    }
}