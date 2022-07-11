package com.jianduanqingchang.securityservice.exception;

/**
 * @author yujie
 */
public class SecurityEntityNotExistException extends RuntimeException {

    public SecurityEntityNotExistException(Class<?> tClass, String username){
        super("Entity: "+tClass.getName().split("Entity")[0]+" not found, id: "+username);
    }
}
