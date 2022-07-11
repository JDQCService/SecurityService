package com.jianduanqingchang.securityservice.authorization.handler;

import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * There is no access token in the header, give some hint to let the user login
 * We extended the AuthenticationEntryPoint just because this framework template we extended believes we send
 *  the username and password in each request, but we are using JWT.
 *
 * @author yujie
 */
public class JwtAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        MyResponseEntity.sendBadOperation(response,"No Such API or no token in header");
    }
}
