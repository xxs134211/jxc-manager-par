package com.xxs.jxcadmin;

import com.xxs.jxcadmin.exceptions.ParamsException;
import com.xxs.jxcadmin.model.RespBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 13421
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ParamsException.class)
    @ResponseBody
    public RespBean paramsExceptionHandler(ParamsException e){
        return RespBean.error(e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RespBean exceptionHandler(Exception e){
        return RespBean.error(e.getMessage());
    }

}
