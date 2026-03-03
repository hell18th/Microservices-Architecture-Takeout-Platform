package com.sky.result;

import com.sky.constant.MessageConstant;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static Result<Void> success() {
        Result<Void> result = new Result<>();
        result.setCode(1);
        result.setMessage(MessageConstant.SUCCESS);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setMessage(MessageConstant.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result<Void> error(String message) {
        Result<Void> result = new Result<>();
        result.setCode(0);
        result.setMessage(message);
        return result;
    }
}