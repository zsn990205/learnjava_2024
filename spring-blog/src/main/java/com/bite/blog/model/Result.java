package com.bite.blog.model;

import com.bite.blog.common.Constants;
import lombok.Data;
@Data
public class Result<T> {
    //200--成功
    //-1---失败
    private int code;//200-成功  -1 失败
    private String errorMsg;
    private T data;

    public static <T> Result<T> success(T data){
        Result result = new Result();
        result.setCode(Constants.RESULT_SUCCESS);
        result.setData(data);
        result.setErrorMsg("");
        return result;
    }

    public static <T> Result<T> fail(String errorMsg){
        Result result = new Result();
        result.setCode(Constants.RESULT_FAIL);
        result.setErrorMsg(errorMsg);
        return result;
    }
    public static <T> Result<T> fail(String errorMsg,T data){
        Result result = new Result();
        result.setCode(Constants.RESULT_FAIL);
        result.setErrorMsg(errorMsg);
        result.setData(data);
        return result;
    }
}
