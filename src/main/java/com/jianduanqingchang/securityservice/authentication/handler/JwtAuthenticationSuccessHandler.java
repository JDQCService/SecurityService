package com.jianduanqingchang.securityservice.authentication.handler;

import com.jianduanqingchang.securityservice.component.JwtComponent;
import com.jianduanqingchang.securityservice.domain.JwtUser;
import com.jianduanqingchang.securityservice.domain.LoginResponseBody;
import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import com.jianduanqingchang.securityservice.utils.AccessAddressUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yujie
 */
@Log4j2
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Login successfully
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // get the IP address from request
        String ip = AccessAddressUtil.getIpAddress(request).orElse("");
        // build token
        JwtUser user = (JwtUser) authentication.getDetails();
        log.info("User Login: " + user.getUsername());
        String token = JwtComponent.createToken(
                String.valueOf(user.getUserId()),
                user.getRole().getRole(),
                user.isRememberMe());
        user.setJwtToken(token);
        // build the response header
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response body
        LoginResponseBody data = new LoginResponseBody(user.getUserId(), user.getRole(), user.getJwtToken());
        MyResponseEntity.sendOk(response, data);
    }
}
