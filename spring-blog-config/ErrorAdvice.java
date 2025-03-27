package com.bite.blog.config;

import com.bite.blog.common.Constants;
import com.bite.blog.model.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//统一异常 AOP
@ResponseBody
@ControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler
    public Result errorHandler(Exception e) {
        Result result = new Result<>();
        result.setErrorMsg("内部发生错误,请联系管理员");
        result.setCode(Constants.RESULT_FAIL);
        return result;
    }
}
