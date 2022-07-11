package com.jianduanqingchang.securityservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


/**
 * JSON format for request and response
 *
 * @author yujie
 * @param <T>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyResponseEntity<T> {

    private static final String CONTENT_TYPE = "application/json;charset=utf-8";

    /**
     * statuc code
     */
    protected Code code;
    
    /**
     * response msg
     * code = 1: ok
     * code = 0: why failed to response
     * code = -1: why the request failed
     * code = -2: invalid token
     */
    protected String msg;
    
    /**
     * JSON body
     */
    protected T data;

    private MyResponseEntity(Code code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private MyResponseEntity(Code code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> MyResponseEntity<T> sendOk(T data) {
        return new MyResponseEntity<>(Code.OK, "ok", data);
    }
    
    public static <T> MyResponseEntity<T> sendBadRequest(String msg) {
        return new MyResponseEntity<>(Code.BAD_REQUEST, msg);
    }
    
    public static <T> MyResponseEntity<T> sendBadOperation(String msg) {
        return new MyResponseEntity<>(Code.BAD_OPERATION, msg);
    }

    public static <T> MyResponseEntity<T> sendTokenCheck(T data){
        return new MyResponseEntity<>(Code.TOKEN_CHECK, "", data);
    }

    public static <T> void sendOk(HttpServletResponse response, T data) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(CONTENT_TYPE);
        OutputStream out = response.getOutputStream();
        out.write(new ObjectMapper().writeValueAsBytes(MyResponseEntity.sendOk(data)));
        out.flush();
        out.close();
    }

    public static void sendBadRequest(HttpServletResponse response, String msg) throws IOException{
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(CONTENT_TYPE);
        OutputStream out = response.getOutputStream();
        out.write(new ObjectMapper().writeValueAsBytes(MyResponseEntity.sendBadRequest(msg)));
        out.flush();
        out.close();
    }

    public static void sendBadOperation(HttpServletResponse response, String msg) throws IOException{
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(CONTENT_TYPE);
        OutputStream out = response.getOutputStream();
        out.write(new ObjectMapper().writeValueAsBytes(MyResponseEntity.sendBadOperation(msg)));
        out.flush();
        out.close();
    }

    public static <T> void sendTokenCheck(HttpServletResponse response, T data) throws IOException{
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(CONTENT_TYPE);
        OutputStream out = response.getOutputStream();
        out.write(new ObjectMapper().writeValueAsBytes(MyResponseEntity.sendTokenCheck(data)));
        out.flush();
        out.close();
    }
}
