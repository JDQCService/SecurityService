package com.jianduanqingchang.securityservice.authentication.handler;

import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Customized authentication failure handler
 *
 * @author yujie
 */
@Log4j2
@Component
public class JwtAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        MyResponseEntity.sendBadOperation(response, "Login failed, please check your username and password");
    }
}
