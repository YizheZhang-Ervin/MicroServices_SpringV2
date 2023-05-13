package com.ervin.demo.microwebserver.exception;

import com.ervin.demo.microwebserver.enums.ReturnCode;
import com.ervin.demo.microwebserver.model.ResultData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@RestControllerAdvice
public class ErrorControllerAdvice {
    //注解：出现异常会来到这个方法处理
    //参数：捕获控制器出现的异常，可传入集合捕获多种类型的异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> handlerError(RuntimeException ex, HandlerMethod hm) {
        System.out.println("统一异常处理");
        System.out.println(ex.getMessage());//异常信息
        System.out.println(hm.getBean().getClass());//哪个类
        System.out.println(hm.getMethod().getName());//在哪个方法
        return ResultData.fail(ReturnCode.RC500.getCode(),ex.getMessage());
    }
}