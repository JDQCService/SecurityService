package com.jianduanqingchang.securityservice.authentication.handler;

import com.jianduanqingchang.securityservice.component.JwtComponent;
import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yujie
 */
@Log4j2
@Component
public class JwtLogoutHandler implements LogoutSuccessHandler {

    @Resource
    private JwtComponent jwtComponent;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        String accessToken = JwtComponent.getTokenFromHeader(request);
        if (accessToken != null) {
            //put token into blacklist
            this.jwtComponent. addBlackList(accessToken);
            log.info("userId: " + JwtComponent.getUserId(accessToken) + " logout");
        } else {
            log.debug("User logout but no token in header");
        }
        MyResponseEntity.sendOk(response,null);
    }
}
