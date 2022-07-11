package com.jianduanqingchang.securityservice.response;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * response status code
 * @author yujie
 */
public enum Code {

    /**
     * request and response success
     */
    OK(1),

    /**
     * successful request but fail to response
     */
    BAD_OPERATION(0),

    /**
     * unsuccessful request
     */
    BAD_REQUEST(-1),

    /**
     * invalid token
     */
    TOKEN_CHECK(-2);
    
    private int code;
    
    Code(int code) {
        this.code = code;
    }
    
    @JsonValue
    public int getCode() {
        return this.code;
    }
    
}
