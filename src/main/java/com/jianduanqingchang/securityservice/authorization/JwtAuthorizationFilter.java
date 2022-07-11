package com.jianduanqingchang.securityservice.authorization;

import com.jianduanqingchang.securityservice.authentication.UsernamePasswordRememberAuthenticationToken;
import com.jianduanqingchang.securityservice.component.JwtComponent;
import com.jianduanqingchang.securityservice.domain.UserRoleEnum;
import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import com.jianduanqingchang.securityservice.service.JwtUserSecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT auth filter
 * check the token is valid or not
 *
 * @author yujie
 */
@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    @Resource
    private JwtComponent jwtComponent;

    @Resource
    private JwtUserSecurityService jwtUserSecurityService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JwtComponent.TOKEN_HEADER_NAME);
        //if the auth process comes here and there is no access_token in the header, it means this api is not authenticated needed
        if (null == token) {
            chain.doFilter(request, response);
            return;
        } else {
            if (!jwtComponent.isValid(token)){
                MyResponseEntity.sendTokenCheck(response, "Invalid Token, login again");
                return;
            }
            if (JwtComponent.needRefresh(token)){
                String newToken = JwtComponent.refreshToken(token);
                this.jwtComponent.addBlackList(token);
                log.info("new token is set, userId: "+ JwtComponent.getUserId(newToken));
                Map<String, Object> data = new HashMap<>(1);
                data.put(JwtComponent.TOKEN_HEADER_NAME, newToken);
                MyResponseEntity.sendTokenCheck(response ,data);
                return;
            }
        }

        /*
        When the code runs here, it means it has passed the validation fo JWT.
        This following is used to deceive the framework that the request has passed the verification,
            which is because we are using jwt but this framework template we extended believes we send
            the username and password in each request.
        */
        SecurityContextHolder.getContext().setAuthentication(this.getAuthentication(token));

        super.doFilterInternal(request, response, chain);
    }
    
    /**
     * Get info from token and build a new auth
     *
     * @param token JWTToken
     * @return Security AuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        var role = UserRoleEnum.getUserRoleByStr(JwtComponent.getUserRoleWithPrefix(token).substring(5));
        return new UsernamePasswordRememberAuthenticationToken(null, null, false,
                jwtUserSecurityService.getAuthorities(role));
    }
}
