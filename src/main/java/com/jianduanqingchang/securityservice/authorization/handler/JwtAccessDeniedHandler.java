package com.jianduanqingchang.securityservice.authorization.handler;

import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author yujie
 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{

        MyResponseEntity.sendBadOperation(response, "No right to access");
    }
}
