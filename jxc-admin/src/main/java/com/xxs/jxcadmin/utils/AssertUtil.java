package com.xxs.jxcadmin.utils;


import com.xxs.jxcadmin.exceptions.ParamsException;

/**
 * @author 13421
 */
public class AssertUtil {


    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
